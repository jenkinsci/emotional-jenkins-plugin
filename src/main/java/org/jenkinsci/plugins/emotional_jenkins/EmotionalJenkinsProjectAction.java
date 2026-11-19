package org.jenkinsci.plugins.emotional_jenkins;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Run;
import java.util.Collection;
import java.util.Collections;
import jenkins.model.TransientActionFactory;

/**
 * Shows Mr. Jenkins on the job page, using the mood of the last completed build that ran the Emotional Jenkins step.
 */
public final class EmotionalJenkinsProjectAction implements Action {

    private final EmotionalJenkinsAction buildAction;

    EmotionalJenkinsProjectAction(EmotionalJenkinsAction buildAction) {
        this.buildAction = buildAction;
    }

    public String getMood() {
        return buildAction.getMood();
    }

    public String getImageFileName() {
        return buildAction.getImageFileName();
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

    @Extension
    @SuppressWarnings("rawtypes")
    public static final class Factory extends TransientActionFactory<Job> {

        @Override
        public Class<Job> type() {
            return Job.class;
        }

        @NonNull
        @Override
        public Collection<? extends Action> createFor(@NonNull Job target) {
            Run<?, ?> last = target.getLastCompletedBuild();
            if (last == null) {
                return Collections.emptySet();
            }
            EmotionalJenkinsAction action = last.getAction(EmotionalJenkinsAction.class);
            if (action == null) {
                return Collections.emptySet();
            }
            return Collections.singleton(new EmotionalJenkinsProjectAction(action));
        }
    }
}
