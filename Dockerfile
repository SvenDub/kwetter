FROM maven:3.5.2-jdk-8

RUN wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN apt-get update && apt-get install --no-install-recommends -y ./google-chrome-stable_current_amd64.deb && apt-get clean
