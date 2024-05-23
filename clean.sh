#!/bin/bash

# Find all directories named "build" or files named ".gradle" in the current directory and its subdirectories
find . \( -name "build" -type d -o -name ".gradle" -type d \) -print0 |

# Delete each directory or file
while IFS= read -r -d '' path; do
    rm -rf "$path"
done
