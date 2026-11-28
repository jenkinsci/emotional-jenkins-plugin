package org.jenkinsci.plugins.emotional_jenkins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;
import org.jvnet.hudson.test.recipes.LocalData;

/**
 * Plugin versions up to 1.2 stored the build result in the {@code result} field of {@link EmotionalJenkinsAction}.
 * Build records from those versions must still load and still show the right mood.
 *
 * <p>The {@code failed-freestyle} job under {@code OldBuildRecordTest/failedBuildFromVersion12LoadsAngry} is a failed
 * freestyle build in the format version 1.2 wrote: its publisher added
 * {@code new EmotionalJenkinsAction(build.getResult())}, which XStream saved as
 * {@code <org.jenkinsci.plugins.emotional__jenkins.EmotionalJenkinsAction><result>FAILURE</result>...}.
 */
@WithJenkins
class OldBuildRecordTest {

    @Test
    @LocalData
    void failedBuildFromVersion12LoadsAngry(JenkinsRule j) throws Exception {
        FreeStyleProject project = j.jenkins.getItemByFullName("failed-freestyle", FreeStyleProject.class);
        assertNotNull(project, "the job from version 1.2 did not load");
        FreeStyleBuild build = project.getBuildByNumber(1);
        assertNotNull(build, "the build from version 1.2 did not load");
        assertEquals(Result.FAILURE, build.getResult());

        EmotionalJenkinsAction action = build.getAction(EmotionalJenkinsAction.class);
        assertNotNull(action, "EmotionalJenkinsAction was not restored");
        assertEquals("angry", action.getMood());
        assertEquals("angry-jenkins.png", action.getImageFileName());

        HtmlPage page = j.createWebClient().getPage(build);
        DomElement mood = page.querySelector("span.emotional-jenkins");
        assertNotNull(mood, "the build page has no mood");
        assertEquals("angry", mood.getAttribute("data-mood"));
    }
}
