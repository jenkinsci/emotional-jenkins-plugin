package org.jenkinsci.plugins.emotional_jenkins;

import hudson.model.Action;
import hudson.model.ProminentProjectAction;
import hudson.model.Result;
import java.util.Collection;
import java.util.Collections;
import jenkins.tasks.SimpleBuildStep;

public final class EmotionalJenkinsAction implements ProminentProjectAction, SimpleBuildStep.LastBuildAction {

    private final Result result;

    public EmotionalJenkinsAction() {
        this(null);
    }

    public EmotionalJenkinsAction(Result result) {
        this.result = result;
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "";
    }

    @Override
    public String getUrlName() {
        return "";
    }

    public Result getResult() {
        return result;
    }

    @Override
    public Collection<? extends Action> getProjectActions() {
        return Collections.singleton(this);
    }
}
