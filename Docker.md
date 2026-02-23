# Supercourse: Docker,Kubernetes, Argo Container Platform 2025
## Docker Quiz
### 1. Docker is a __________ Technology?
A. Virtualization 

B. Bare Metal 

**\*C. Container**

D. None of the above 
### 2. Which of the following is not a part of Docker Platform?
A. Dockerfile

B. Compose 

C. Swarm 
  
**\*D. Fleet**
### 3. Which of the following statement is wrong? "Each container could have its own..."
A. Network interface

B. Root file system

C. Hostname

**\*D. Kernel**
### 4. Docker is not availble for?
A. Windows 10

**\*B. Windows 7**

C. Ubuntu 17.04

D. Ubuntu 14.04
### 5. Container technology uses virtualized hardware. True or False?
A. True

**\*B. False**
### 6. Which of these underlying technologies make running process in isolation (contained) possible?
**\*A. Linux namespaces**

B. Dedicated hardware

C. Container libraries

D. None of the above
### 7. How do you run docker commands without escalated privileges?
A. Change the file permissions of Docker binary

B. Run it as a root user

C. Change permissions of Docker Socket

**\*D. Add user to Docker Group**
### 8. Docker images are typically stored and distributed with ?
A. Github

B. Bit Bucket

**\*C. Docker Hub**

D. Docker Market
### 9. Registries are stored in repositories of Dockerhub. True or False?
A. True

**\*B. False**
### 10. Which tool is not a part of Docker Orchestration?
A. Docker Machine

B. Docker Swarm

C. Docker Compose

**\*D. Kitematics**
### 11. What file system does Docker use?
A. `extfs`

**\*B.`overlayfs`**

C. `ntfs`

D. `xfs`
### 12. Docker uses `lxc` in the latest release as the core engine?
A. True

**\*B. False**

### 13. Docker is written in which language?
A. Python

**\*B. Go**

C. Java

D. Ruby
### 14. Which of the following commands could be used to connect to a running container to execute commands (similar to SSH connection) ?
A. `docker exec -it <container_id>`

**\*B. `docker exec -it <container_id> bash`**

C. `docker run -it <container_name>`

D. None of the above
### 15. Docker events command will print the logs for the application running inside the container. True or False?
A. True

**\*B. False**
### 16. Docker stop will wipe out all the changes you have made since launching the container ?
A. True

**\*B. False**
### 17. Which option makes docker containers to persist?
A. `-i`

B. `-d`

C. `-t`

**\*D. None of the above**
### 18. Docker virtualizes memory, processor etc. through namespace for each container 
A. True

**\*B. False**
### 19. Kalam has launched a container with an image specification "schoolofdevops/testapp". Can you identify the tag with which Kalam launched the container with ?
A. testapp

B. schoolofdevops

**\*C. latest**

D. v1.0.1
### 20. Which of the following option is used for following(continuously update) logs of a running container?
A. `-c`

**B. `-f`**

C. `-follow`

D. `-continue`
### 21. You have to connect to the container in order to launch an application. True or False?
A. True

**\*B. False**
### 22. Which of the following state comes next in the sequence of system events while launching a container? "Pull -> Create -> Attach -> ?"
A. Attach

**\*B. Network Connect**

C. Execute

D. Die
### 23. Which of the following component is been open sourced by Docker Inc. ?
**\*A. `containerd`**

B. `runc`

C. `oci`

D. None of the above
### 24. What is the starting range of ports that  Docker maps to a container when you use -P option?
A. 32758

B. 35362

C. 32678

**\*D. 32768**
### 25. What is the command to rename a container?
**\*A. `docker container rename <container_id/name> <new_name>`**

B. `docker rename container <container_id/name> <new_name>`

C. `docker rename container <new_name> <container_id/name>`

D. None of the above
### 26. What does docker container attach command do?
**\*A. It attaches to the process running inside the container**

B. It prints the logs of the container

C. It attaches to the shell inside the container

D. It gives resource utilization data

### 27. How do you check the log path of a container?
**\*A. Using docker container inspect command**

B. Using docker container logs command

C. Both A and B

D. None of the above
### 28. What is the command used to monitor docker resource utilization?
A. `docker container top`

B. `docker top`

**\*C. `docker container stats`**

D. `docker system stats`
### 29. It is not possible to change the Memory and CPU limits of a container while it is running. True or False?
A. True

**\*B. False**
### 30. How to update memory limit of a running container?
A. `docker container limit -m <memory_size>`

**\*B. `docker container update -M <memory_size>`**

C. `docker memory --limit <memory_size>`

D. `docker resize --limit -m <memory_size>`
### 31. A, B and C are 3 containers running on a system. Container A has a CPU share of 1024, Container B and C has 512 CPU Shares. Which Container has the lowest CPU allocation?
A. Container A

B. Container B

C. Container C

D. Container B & C
### 32. Which of the following statement is true?
A. Docker image and container are the same concept

B. Docker container can run without an image too

**\*C. Docker images are the pre baker template for running docker containers**

D. Docker container are the pre required to to run images
### 33. It is recommended to use docker run over docker container run which is going to be deprecated. True or False.
A. True

**\*B. False**
