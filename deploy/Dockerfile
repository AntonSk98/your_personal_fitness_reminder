# Use openjdk base image
FROM openjdk:latest

#Environment variables
ENV PATH_TO_CONFIG=/fitness_bot/properties.yml
ENV PATH_TO_EXERCISES=/fitness_bot/exercises

# Set the working directory to /fitness_bot
WORKDIR /fitness_bot

# Copy files into the container under workdir
COPY your_personal_fitness_reminder-1.1.jar your_personal_fitness_reminder-1.1.jar
COPY exercises exercises
COPY properties.yml properties.yml

# Set the command to run your JAR file
CMD ["java", "-jar", "your_personal_fitness_reminder-1.1.jar"]