# ReCollect
Web-server application to index, transcribe and search video recordings. Useful for locating information discussed in past meetings.
Developed as a learning project.

## Screenshots
![Home page](screenshots/homepage.png)
![Search results](screenshots/search-results.png)

## Tech Stack
 - Java 21
 - Spring Boot
 - JPA, Hibernate & H2
 - [Mustache templates](https://mustache.github.io/)
 - [sakura css template](https://oxal.org/projects/sakura/demo/)
 - [FFMPEG](https://ffmpeg.org/) (reading information about video files and generate thumbnails)
 - [Whisper AI](https://github.com/openai/whisper) (transcribe recordings)

## To-Do list
 - [x] Home page
 - [x] Search results page
 - [ ] Recording details page
 - [ ] Upload page
 - [ ] Integrate with an external Full-Text-Search engine (Elastic Search, SQLite, etc...) for better search results
 - [ ] Transcribe new recordings automatically using WhisperAI command line tool
 - [ ] Generate and store thumbnails with FFMPEG