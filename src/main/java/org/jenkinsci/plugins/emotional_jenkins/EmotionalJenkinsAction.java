package org.jenkinsci.plugins.emotional_jenkins;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.model.Result;
import hudson.model.Run;
import jenkins.model.RunAction2;

/**
 * Attached to a build by {@link EmotionalJenkinsPublisher}. The mood is computed from the build result when the page
 * is rendered, so a Pipeline step called from {@code post { always { ... } }} still reflects the final result.
 */
public final class EmotionalJenkinsAction implements RunAction2 {

    /** Result stored by plugin versions up to 2.x; kept so old build records still load. */
    @SuppressFBWarnings(value = "UWF_UNWRITTEN_FIELD", justification = "Populated by XStream from old build records")
    private Result result;

    private transient Run<?, ?> run;

    public EmotionalJenkinsAction() {}

    @Override
    public void onAttached(Run<?, ?> r) {
        this.run = r;
    }

    @Override
    public void onLoad(Run<?, ?> r) {
        this.run = r;
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String getUrlName() {
        return null;
    }

    /** @return the build result, or {@code null} while the build is still running without a result. */
    public Result getResult() {
        if (run != null && run.getResult() != null) {
            return run.getResult();
        }
        return result;
    }

    /** @return the image Mr. Jenkins should show for the current result. */
    public String getImageFileName() {
        return Mood.of(getResult()).imageFileName;
    }

    /** @return {@code happy}, {@code worried}, {@code angry} or {@code disappointed}. */
    public String getMood() {
        return Mood.of(getResult()).name().toLowerCase(java.util.Locale.ROOT);
    }

    enum Mood {
        HAPPY("happy-jenkins.png"),
        WORRIED("worried-jenkins.png"),
        ANGRY("angry-jenkins.png"),
        DISAPPOINTED("disappointed-jenkins.png");

        final String imageFileName;

        Mood(String imageFileName) {
            this.imageFileName = imageFileName;
        }

        static Mood of(Result result) {
            if (Result.FAILURE.equals(result)) {
                return ANGRY;
            }
            if (Result.UNSTABLE.equals(result)) {
                return WORRIED;
            }
            if (Result.ABORTED.equals(result) || Result.NOT_BUILT.equals(result)) {
                return DISAPPOINTED;
            }
            return HAPPY;
        }
    }
}
