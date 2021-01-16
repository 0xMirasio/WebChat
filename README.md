# WebChat

to start project : </br>

cd AgentChat/ </br>
chmod +x *.sh </br>
./build.sh
then </br>
./start.sh

If you want to clear your cache sessions (Every time you start a chat sessions with another person, a session is stored in .cache/sessions, it can lead to lag in application (DB parsing)) </br>

./clear.sh

KNOW-BUG : </br>

- if you close a chat session jframe (from server side), if you reopen the same chat session the jframe will completely bug and nothing will be printed, even is message is receved. </br>
Solution : restart application </br>

- Similar to the previous bug (jframe problem), if a person A contact B, then a person C contact B, the message from C will not pop up on the jframe created for the chat session (B-C) and will go on the chat session for (A-B). The first JFrame is taking all the content, the others jframes will not get any informations. </br>

Solution : No solution found </br>
