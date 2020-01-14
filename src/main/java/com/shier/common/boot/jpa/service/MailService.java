package com.shier.common.boot.jpa.service;

/**
 * @Author: liyunbiao
 * @Date: 2019/9/4 2:20 PM
 * @description
 */
public interface MailService {
    /**
     * 发送邮件
     *
     * @param to      目的地
     * @param subject 主题
     * @param content 内容
     */
    void sendEmail(String to, String subject, String content);

    /**
     * 发送邮件
     *
     * @param to       目的地
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件
     * @param fileName 附件名
     */
    void sendAnnexEmail(String to, String subject, String content, String filePath, String fileName);

    /**
     * 功能描述：发送带图片的邮件
     *
     * @param to      发送目标邮箱
     * @param subject 邮件标题
     * @param content 邮件内容
     * @param imgPath 图片路径
     */
    void sendImageMail(String to, String subject, String content, String imgPath);
}
