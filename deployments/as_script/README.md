Tcom - deployment as a script with predefined folder structure
==============================================================

# Prepare
Main ommited part in this folder structure is fat jar at lib directory. 
Once it copied from target folder after running 'sbt assembly',
theoretically it's enough to run this app.

# Usage
Example of running the script with arguments:
```shell
bash ./bin/startapp.sh --srvhost localhost --srvport 1661 --message tst1 --frequencymsecs 1000 --maxburstcount 10
```

NOTE: There is nothing to prevent from copy this dir structure into remote server and start the app from there.

