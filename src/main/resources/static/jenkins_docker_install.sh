# This file is about the trouble I was having that the provided Jenkins Docker image had Java 8 and I needed Java 14
cd /opt/java/

curl https://download.java.net/java/GA/jdk14.0.2/205943a0976c4ed48cb16f1043c5c647/12/GPL/openjdk-14.0.2_linux-x64_bin.tar.gz -o openjdk-14.0.2_linux-x64_bin.tar.gz

tar xvf openjdk-14.0.2_linux-x64_bin.tar.gz

rm openjdk-14.0.2_linux-x64_bin.tar.gz

# new jdk is installed in /opt/java/jdk-14.0.2
# export JAVA_HOME=/opt/java/jdk-14.0.2
# export PATH="${JAVA_HOME}/bin:${PATH}"

# pom.xml hack:
#			<plugin>
#		        <groupId>org.apache.maven.plugins</groupId>
#        		<artifactId>maven-surefire-plugin</artifactId>
#		        <configuration>
#					<jvm>/opt/java/jdk-14.0.2/bin/java</jvm>
#		        </configuration>
#      		</plugin>

# Jenkins Maven properties hack:
# maven.compiler.fork=true
# maven.compiler.executable=/opt/java/jdk-14.0.2/bin/javac

