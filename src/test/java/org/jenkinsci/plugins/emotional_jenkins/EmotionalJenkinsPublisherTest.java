package org.jenkinsci.plugins.emotional_jenkins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.tasks.Builder;
import org.htmlunit.html.HtmlPage;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.FailureBuilder;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.UnstableBuilder;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class EmotionalJenkinsPublisherTest {

    @Test
    void freestyleSuccessKeepsBuildGreenAndIsHappy(JenkinsRule j) throws Exception {
        FreeStyleBuild build = runFreestyle(j, null, Result.SUCCESS);
        assertEquals("happy", build.getAction(EmotionalJenkinsAction.class).getMood());
    }

    @Test
    void freestyleUnstableIsWorried(JenkinsRule j) throws Exception {
        FreeStyleBuild build = runFreestyle(j, new UnstableBuilder(), Result.UNSTABLE);
        assertEquals("worried", build.getAction(EmotionalJenkinsAction.class).getMood());
    }

    @Test
    void freestyleFailureIsAngry(JenkinsRule j) throws Exception {
        FreeStyleBuild build = runFreestyle(j, new FailureBuilder(), Result.FAILURE);
        assertEquals("angry", build.getAction(EmotionalJenkinsAction.class).getMood());
    }

    @Test
    void configRoundTrip(JenkinsRule j) throws Exception {
        FreeStyleProject p = j.createFreeStyleProject();
        p.getPublishersList().add(new EmotionalJenkinsPublisher());
        j.configRoundtrip(p);
        assertInstanceOf(EmotionalJenkinsPublisher.class, p.getPublishersList().get(0));
    }

    @Test
    void declarativePipelinePostAlwaysReflectsFinalResult(JenkinsRule j) throws Exception {
        WorkflowJob p = j.createProject(WorkflowJob.class, "declarative");
        p.setDefinition(new CpsFlowDefinition("""
                pipeline {
                  agent none
                  stages {
                    stage('Test') { steps { error 'boom' } }
                  }
                  post { always { emotionalJenkins() } }
                }
                """, true));
        WorkflowRun run = j.buildAndAssertStatus(Result.FAILURE, p);
        assertEquals("angry", run.getAction(EmotionalJenkinsAction.class).getMood());
    }

    @Test
    void abortedPipelineIsDisappointed(JenkinsRule j) throws Exception {
        WorkflowJob p = j.createProject(WorkflowJob.class, "aborted");
        p.setDefinition(new CpsFlowDefinition("currentBuild.result = 'ABORTED'\nemotionalJenkins()", true));
        WorkflowRun run = j.buildAndAssertStatus(Result.ABORTED, p);
        EmotionalJenkinsAction action = run.getAction(EmotionalJenkinsAction.class);
        assertEquals("disappointed", action.getMood());
        assertEquals("disappointed-jenkins.png", action.getImageFileName());
    }

    @Test
    void everyResultMapsToAMood() {
        assertEquals(EmotionalJenkinsAction.Mood.HAPPY, EmotionalJenkinsAction.Mood.of(null));
        assertEquals(EmotionalJenkinsAction.Mood.HAPPY, EmotionalJenkinsAction.Mood.of(Result.SUCCESS));
        assertEquals(EmotionalJenkinsAction.Mood.WORRIED, EmotionalJenkinsAction.Mood.of(Result.UNSTABLE));
        assertEquals(EmotionalJenkinsAction.Mood.ANGRY, EmotionalJenkinsAction.Mood.of(Result.FAILURE));
        assertEquals(EmotionalJenkinsAction.Mood.DISAPPOINTED, EmotionalJenkinsAction.Mood.of(Result.ABORTED));
        assertEquals(EmotionalJenkinsAction.Mood.DISAPPOINTED, EmotionalJenkinsAction.Mood.of(Result.NOT_BUILT));
    }

    @Test
    void scriptedPipelineUnstableIsWorried(JenkinsRule j) throws Exception {
        WorkflowJob p = j.createProject(WorkflowJob.class, "scripted");
        p.setDefinition(new CpsFlowDefinition("unstable 'flaky'\nemotionalJenkins()", true));
        WorkflowRun run = j.buildAndAssertStatus(Result.UNSTABLE, p);
        assertEquals("worried", run.getAction(EmotionalJenkinsAction.class).getMood());
    }

    @Test
    void buildAndJobPagesShowMood(JenkinsRule j) throws Exception {
        FreeStyleBuild build = runFreestyle(j, new FailureBuilder(), Result.FAILURE);
        try (JenkinsRule.WebClient wc = j.createWebClient()) {
            wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
            HtmlPage buildPage = wc.getPage(build);
            assertMarker(buildPage);
            HtmlPage jobPage = wc.getPage(build.getParent());
            assertMarker(jobPage);
        }
    }

    private static void assertMarker(HtmlPage page) {
        var marker = page.querySelector(".emotional-jenkins");
        assertNotNull(marker, "mood marker missing on " + page.getUrl());
        assertEquals(1, page.querySelectorAll(".emotional-jenkins").size(), "exactly one marker on " + page.getUrl());
        assertEquals("angry", marker.getAttributes().getNamedItem("data-mood").getNodeValue());
        assertTrue(marker.getAttributes()
                .getNamedItem("data-image")
                .getNodeValue()
                .endsWith("/plugin/emotional-jenkins-plugin/images/angry-jenkins.png"));
    }

    private static FreeStyleBuild runFreestyle(JenkinsRule j, Builder builder, Result expected) throws Exception {
        FreeStyleProject p = j.createFreeStyleProject();
        if (builder != null) {
            p.getBuildersList().add(builder);
        }
        p.getPublishersList().add(new EmotionalJenkinsPublisher());
        return j.buildAndAssertStatus(expected, p);
    }
}
