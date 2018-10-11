package com.kalix.middleware.excel.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kalix.framework.core.api.web.model.BaseDTO;
import com.kalix.middleware.excel.api.annotation.ExcelField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zangyanming on 2018/9/13.
 */

public class ChoiceDto extends BaseDTO {
    @Column(nullable = false)
    private String type;               // 类别，字典[类别]
    @Lob
    @Column(nullable = false)
    private String stem;               // 题干
    @Column(nullable = false)
    private String answerA;            // 选项A
    private String answerB;            // 选项B
    private String answerC;            // 选项C
    private String answerD;            // 选项D
    private String answerE;            // 选项E
    private String answerF;            // 选项F
    private String answerG;            // 选项G
    private String answer;             // 正确选项
    private String analysis;           // 试题解析
    private String checkFlag = "0";    // 审核状态，字典[审核状态]
    private Long checkerId;            // 审核员id
    private String checker;            // 审核员
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkDate;            // 审核时间
    private String checkReason;        // 审核不通过原因
    private String repeatedFlag = "0"; // 题库排重标识

    @ExcelField(title = "类别",type=0, align = 1, dictType = "类别", sort = 10)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ExcelField(title = "题干", type=0,align = 1, sort = 20)
    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    @ExcelField(title = "选项A",type=0, align = 1, sort = 30)
    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    @ExcelField(title = "选项B",type=0, align = 1, sort = 40)
    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    @ExcelField(title = "选项C",type=0, align = 1, sort = 50)
    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    @ExcelField(title = "选项D", type=0,align = 1, sort = 60)
    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    @ExcelField(title = "选项E",type=0, align = 1, sort = 70)
    public String getAnswerE() {
        return answerE;
    }

    public void setAnswerE(String answerE) {
        this.answerE = answerE;
    }

    @ExcelField(title = "选项F", type=0,align = 1, sort = 80)
    public String getAnswerF() {
        return answerF;
    }

    public void setAnswerF(String answerF) {
        this.answerF = answerF;
    }

    @ExcelField(title = "选项G",type=0, align = 1, sort = 90)
    public String getAnswerG() {
        return answerG;
    }

    public void setAnswerG(String answerG) {
        this.answerG = answerG;
    }

    @ExcelField(title = "答案",type=0, align = 1, sort = 100)
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @ExcelField(title = "试题解析", type=0,align = 1, sort = 110)
    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }

    public Long getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(Long checkerId) {
        this.checkerId = checkerId;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(String checkReason) {
        this.checkReason = checkReason;
    }

    public String getRepeatedFlag() {
        return repeatedFlag;
    }

    public void setRepeatedFlag(String repeatedFlag) {
        this.repeatedFlag = repeatedFlag;
    }
}
