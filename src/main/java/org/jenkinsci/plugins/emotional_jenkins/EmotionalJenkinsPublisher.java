package org.jenkinsci.plugins.emotional_jenkins;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Post-build action / Pipeline step that lets Mr. Jenkins react to the build result.
 *
 * <p>In Pipeline it is available as {@code emotionalJenkins()}.
 */
public class EmotionalJenkinsPublisher extends Notifier implements SimpleBuildStep {

    @DataBoundConstructor
    public EmotionalJenkinsPublisher() {}

    @Override
    public boolean requiresWorkspace() {
        return false;
    }

    @Override
    public void perform(@NonNull Run<?, ?> run, @NonNull EnvVars env, @NonNull TaskListener listener) {
        if (run.getAction(EmotionalJenkinsAction.class) == null) {
            run.addAction(new EmotionalJenkinsAction());
        }
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Extension
    @Symbol("emotionalJenkins")
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @NonNull
        @Override
        public String getDisplayName() {
            return "Emotional Jenkins";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
