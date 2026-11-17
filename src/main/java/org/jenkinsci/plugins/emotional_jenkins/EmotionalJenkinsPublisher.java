package org.jenkinsci.plugins.emotional_jenkins;

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
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest2;

public class EmotionalJenkinsPublisher extends Notifier implements SimpleBuildStep {

    @Override
    public void perform(Run<?, ?> run, EnvVars env, TaskListener listener) {
        run.addAction(new EmotionalJenkinsAction(run.getResult()));
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public BuildStepDescriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
    }

    @Extension
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        public DescriptorImpl() {
            super(EmotionalJenkinsPublisher.class);
        }

        @Override
        public String getDisplayName() {
            return "Emotional Jenkins";
        }

        @Override
        public Publisher newInstance(StaplerRequest2 req, JSONObject formData) throws FormException {
            return new EmotionalJenkinsPublisher();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
