# Nice_HW - Task Suggestion System

## Overview
It is a REST API that suggests tasks based on user input. It supports two modes:
- **Dictionary-based (`suggest`)**: Matches phrases to tasks using a dictionary.
- **Classifier-based (`classifier`)**: Uses a machine learning model (logistic regression) to predict tasks.

"Developers" users (specific IDs in resources/devUsers) can update the dictionary dynamically using special commands.

---

## Key Features

- Suggest tasks for user utterances.
- Two modes: dictionary and classifier.
- Developer dictionary updates.
- Task activation after suggestion.
- Returns structured JSON responses with task name and timestamp.
- Health check endpoint.

---

## Installation

- bash
- git clone <repo_url>
- cd Nice_HW
- ./mvnw clean install
- ./mvnw spring-boot:run (make sure that http://localhost:8080/ is available)

---

## Usage

- POST /suggestTask
Request:

{
  "utterance": "I forgot my password",
  "userId": "user1",
  "sessionId": "sess1",
  "timestamp": "2025-09-11T10:00:00Z"
}

*Optional query parameter: mode = suggest | classifier

*Developer command format: "update dictionary - <synonym>,<TaskName>" returns NoTaskFound.

- Response:

{
  "task": "ResetPasswordTask",
  "timestamp": "2025-09-11T13:00:00+03:00"
}

- GET /suggestTask – Health check returns "SuggestTask API is running!".

- Testing:

Run with "./mvnw test".
