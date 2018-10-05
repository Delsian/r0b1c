#!/usr/bin/make

DIRS = bootloader controller
DIRSCLEAN = $(addsuffix .clean,$(DIRS))

all:
	@ $(MAKE) -C bootloader
	@echo Building r0b1c
	@ $(MAKE) -C controller

clean: $(DIRSCLEAN)

$(DIRSCLEAN): %.clean:
	@echo Cleaning $*
	@ $(MAKE) -C $*  clean

bootloader:
	@ $(MAKE) -C bootloader

flash:
	@ $(MAKE) -C controller flash

dfu:
	@ $(MAKE) -C controller dfu

.PHONY: all $(DIRS) $(DIRSCLEAN) bootloader flash dfu
