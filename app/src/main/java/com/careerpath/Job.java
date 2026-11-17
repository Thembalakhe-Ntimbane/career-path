package com.careerpath;

final class Job {
    final String title;
    final String sector;
    final String summary;
    final String subjects;
    final String pathway;
    final String salary;

    Job(String title, String sector, String summary, String subjects, String pathway, String salary) {
        this.title = title;
        this.sector = sector;
        this.summary = summary;
        this.subjects = subjects;
        this.pathway = pathway;
        this.salary = salary;
    }
}
