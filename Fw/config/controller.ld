/* Linker script to configure memory regions. */

SEARCH_DIR(.)
GROUP(-lgcc -lc -lnosys)

MEMORY
{
  FLASH (rx) : ORIGIN = 0x26000, LENGTH = 0x42000
  RAM (rwx) :  ORIGIN = 0x20003000, LENGTH = 0xD000
}

INCLUDE "nrf52.ld"
