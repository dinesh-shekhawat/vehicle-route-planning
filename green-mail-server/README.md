1. Download the Green Mail Standalone JAR
https://greenmail-mail-test.github.io/greenmail/#download_2_1_x

2. Command to download
wget https://repo1.maven.org/maven2/com/icegreen/greenmail-standalone/2.1.0-alpha-3/greenmail-standalone-2.1.0-alpha-3.jar

3. Run
java -Dgreenmail.smtp.port=3025 -Dgreenmail.imap.port=3143 -Dgreenmail.hostname=0.0.0.0 -jar greenmail-standalone.jar	
