package com.micheal.print;

/**
 * Created by Lipeng on 2018/4/24.
 */

public class Label {
    private String projectName;
    private String sampleCode;
    private String sampleAddress;
    private String sampler;
    private String sampleDate;
    private String monItemNames;
    private int state;

    public String getProjectName() {
        return projectName;
    }

    public Label setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public Label setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
        return this;
    }

    public String getSampleAddress() {
        return sampleAddress;
    }

    public Label setSampleAddress(String sampleAddress) {
        this.sampleAddress = sampleAddress;
        return this;
    }

    public String getSampler() {
        return sampler;
    }

    public Label setSampler(String sampler) {
        this.sampler = sampler;
        return this;
    }

    public String getSampleDate() {
        return sampleDate;
    }

    public Label setSampleDate(String sampleDate) {
        this.sampleDate = sampleDate;
        return this;
    }

    public String getMonItemNames() {
        return monItemNames;
    }

    public Label setMonItemNames(String monItemNames) {
        this.monItemNames = monItemNames;
        return this;
    }

    public int getState() {
        return state;
    }

    public Label setState(int state) {
        this.state = state;
        return this;
    }
}
