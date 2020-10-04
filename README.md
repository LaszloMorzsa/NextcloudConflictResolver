# Conflict Resolver for Nextcloud desktop client
###### Version: 0.0.1.  
###### Date: 2020.10.04.  

### What is it for?
If you have a lot of file conflicts after Nextcloud synchronization, you can use these tools to resolve them. No special logic is built in, the downloaded (old version) from the server will be overwritten with the latest version stored locally.

### How to use?
There are two option:
**-p** for target path, default current path.
**-r** for recursive scan and replace, not recursive by default.

Example for recursive call with target path:

    java -jar ConflictResolver.jar -r -p c:\Users\user\Documents\

