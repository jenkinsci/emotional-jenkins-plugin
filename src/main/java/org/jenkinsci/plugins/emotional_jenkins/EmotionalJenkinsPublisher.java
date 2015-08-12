package org.jenkinsci.plugins.emotional_jenkins;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import jenkins.model.TransientActionFactory;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;

public class EmotionalJenkinsPublisher extends Notifier implements SimpleBuildStep {

    @DataBoundConstructor
    public EmotionalJenkinsPublisher() {}

    @Override
    public void perform(Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
/*
        DESCRIPTOR.counter++;
        int value = DESCRIPTOR.counter % 5;
        switch (value) {
            case 0:
                build.setResult(Result.FAILURE);
                break;
            case 1:
                build.setResult(Result.UNSTABLE);
                break;
            case 2:
                build.setResult(Result.ABORTED);
                break;
            case 3:
                build.setResult(Result.NOT_BUILT);
                break;
            default:
        }
*/

        build.addAction(new EmotionalJenkinsAction(build.getResult()));
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Extension public static final class LastBuildActionFactory extends TransientActionFactory<Job> {

        @Override public Class<Job> type() {
            return Job.class;
        }

        @Override public Collection<? extends Action> createFor(Job j) {
            Run r = j.getLastBuild();
            if (r != null) {
                EmotionalJenkinsAction a = r.getAction(EmotionalJenkinsAction.class);
                if (a != null) {
                    return Collections.singleton(a);
                }
            }
            return Collections.emptySet();
        }

    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
//        public static int counter = 0;

        public String getDisplayName() {
            return "Emotional Jenkins";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
