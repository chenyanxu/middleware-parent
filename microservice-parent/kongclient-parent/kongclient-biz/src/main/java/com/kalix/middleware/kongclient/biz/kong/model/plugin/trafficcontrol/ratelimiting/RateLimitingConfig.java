package com.kalix.middleware.kongclient.biz.kong.model.plugin.trafficcontrol.ratelimiting;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

/**
 * Created by vaibhav on 17/06/17.
 */
//@Data
public class RateLimitingConfig {

    @SerializedName("second")
    Integer second;
    @SerializedName("minute")
    Integer minute;
    @SerializedName("hour")
    Integer hour;
    @SerializedName("day")
    Integer day;
    @SerializedName("month")
    Integer month;
    @SerializedName("year")
    Integer year;
    @SerializedName("limit_by")
    LimitBy limitBy;
    @SerializedName("policy")
    Policy policy;
    @SerializedName("fault_tolerant")
    Boolean faultTolerant;
    @SerializedName("redis_host")
    String redisHost;
    @SerializedName("redis_port")
    Integer redisPort;
    @SerializedName("redis_password")
    String redisPassword;
    @SerializedName("redis_timeout")
    Integer redisTimeout;
    @SerializedName("redis_database")
    Integer redisDatabase;

    public enum Policy {
        local, cluster, redis
    }

    public enum LimitBy {
        consumer, credential, ip
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LimitBy getLimitBy() {
        return limitBy;
    }

    public void setLimitBy(LimitBy limitBy) {
        this.limitBy = limitBy;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Boolean getFaultTolerant() {
        return faultTolerant;
    }

    public void setFaultTolerant(Boolean faultTolerant) {
        this.faultTolerant = faultTolerant;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public Integer getRedisTimeout() {
        return redisTimeout;
    }

    public void setRedisTimeout(Integer redisTimeout) {
        this.redisTimeout = redisTimeout;
    }

    public Integer getRedisDatabase() {
        return redisDatabase;
    }

    public void setRedisDatabase(Integer redisDatabase) {
        this.redisDatabase = redisDatabase;
    }
}
