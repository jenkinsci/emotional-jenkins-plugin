package org.jenkinsci.plugins.emotional_jenkins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import javaposse.jobdsl.plugin.ExecuteDslScripts;
import javaposse.jobdsl.plugin.RemovedJobAction;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

/**
 * Job DSL (and therefore Configuration as Code, which creates jobs through Job DSL scripts) can add the publisher
 * using its {@code emotionalJenkins} symbol.
 */
@WithJenkins
class JobDslTest {

    @Test
    void freestyleJobCreatedWithJobDslUsesThePublisher(JenkinsRule j) throws Exception {
        FreeStyleProject seed = j.createFreeStyleProject("seed");
        ExecuteDslScripts dsl = new ExecuteDslScripts();
        dsl.setScriptText("""
                freeStyleJob('created-by-dsl') {
                    publishers {
                        emotionalJenkins()
                    }
                }
                """);
        dsl.setUseScriptText(true);
        dsl.setRemovedJobAction(RemovedJobAction.IGNORE);
        seed.getBuildersList().add(dsl);
        j.buildAndAssertSuccess(seed);

        FreeStyleProject created = j.jenkins.getItemByFullName("created-by-dsl", FreeStyleProject.class);
        assertNotNull(created, "Job DSL did not create the job");
        assertInstanceOf(
                EmotionalJenkinsPublisher.class, created.getPublishersList().get(0));

        FreeStyleBuild build = j.buildAndAssertStatus(Result.SUCCESS, created);
        assertEquals("happy", build.getAction(EmotionalJenkinsAction.class).getMood());
    }
}
