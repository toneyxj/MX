package com.moxi.studentclient.model;

/**
 * Created by Administrator on 2016/11/3.
 */

public class VoteBean {
    public Long id;
    public String option;
    public String vote;
    public Long voteId;
    public String name;
    public boolean checked=false;

    public VoteBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }
}
