package edu.northeastern.csye6220.vehiclerouteplanning.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("email")
public class EmailProperties {

	private String smtpHost = "email-server";
	private int smtpPort = 25;
	private String fromEmailAddress = "<admin@drivesync.com>";
	private String fromEmailUsername = "Admin";
	
	private String smtpHostPropertyKey = "mail.smtp.host";
	private String smtpPortPropertyKey = "mail.smtp.port";
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public int getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String getFromEmailAddress() {
		return fromEmailAddress;
	}
	public void setFromEmailAddress(String fromEmailAddress) {
		this.fromEmailAddress = fromEmailAddress;
	}
	public String getSmtpHostPropertyKey() {
		return smtpHostPropertyKey;
	}
	public void setSmtpHostPropertyKey(String smtpHostPropertyKey) {
		this.smtpHostPropertyKey = smtpHostPropertyKey;
	}
	public String getSmtpPortPropertyKey() {
		return smtpPortPropertyKey;
	}
	public void setSmtpPortPropertyKey(String smtpPortPropertyKey) {
		this.smtpPortPropertyKey = smtpPortPropertyKey;
	}
	public String getFromEmailUsername() {
		return fromEmailUsername;
	}
	public void setFromEmailUsername(String fromEmailUsername) {
		this.fromEmailUsername = fromEmailUsername;
	}
	
	@Override
	public String toString() {
		return "EmailProperties [smtpHost=" + smtpHost + ", smtpPort=" + smtpPort + ", fromEmailAddress="
				+ fromEmailAddress + ", fromEmailUsername=" + fromEmailUsername + ", smtpHostPropertyKey="
				+ smtpHostPropertyKey + ", smtpPortPropertyKey=" + smtpPortPropertyKey + "]";
	}
}
