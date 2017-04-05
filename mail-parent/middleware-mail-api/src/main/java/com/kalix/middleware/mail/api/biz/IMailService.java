package com.kalix.middleware.mail.api.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.middleware.mail.api.MailConfig;
import com.kalix.middleware.mail.api.MailContent;

import java.util.Properties;

/**
 * @author sunlf
 */
public interface IMailService {

    Properties mailInit(MailConfig config);

    JsonStatus sendMail(MailContent content);
}
