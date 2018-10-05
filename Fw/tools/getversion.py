#!/usr/bin/python
import os, subprocess

if __name__ == '__main__':
    tag = subprocess.check_output(['git', 'describe', '--tags', '--abbrev=0']).rstrip("\r\n")
    print tag
    with open("version.txt", 'w') as fver:
        fver.write(tag)
        fver.close()
