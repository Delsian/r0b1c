/* Linker script to configure memory regions. */

SEARCH_DIR(.)
GROUP(-lgcc -lc -lnosys)

MEMORY
{
  FLASH (rx) : ORIGIN = 0x64000, LENGTH = 0x1A000
  RAM (rwx) :  ORIGIN = 0x20003000, LENGTH = 0xd000
  
  uicr_bootloader_start_address (r) : ORIGIN = 0x10001014, LENGTH = 0x4
  bootloader_settings_page (r) : ORIGIN = 0x0007F000, LENGTH = 0x1000
  mbr_params_page (r) : ORIGIN = 0x0007E000, LENGTH = 0x1000
  uicr_mbr_params_page (r) : ORIGIN = 0x10001018, LENGTH = 0x4
}

SECTIONS
{
  .uicr_bootloader_start_address :
  {
    PROVIDE(__start_uicr_bootloader_start_address = .);
    KEEP(*(SORT(.uicr_bootloader_start_address*)))
    PROVIDE(__stop_uicr_bootloader_start_address = .);
  } > uicr_bootloader_start_address
  .bootloader_settings_page(NOLOAD) :
  {
    PROVIDE(__start_bootloader_settings_page = .);
    KEEP(*(SORT(.bootloader_settings_page*)))
    PROVIDE(__stop_bootloader_settings_page = .);
  } > bootloader_settings_page
  .mbr_params_page(NOLOAD) :
  {
    PROVIDE(__start_mbr_params_page = .);
    KEEP(*(SORT(.mbr_params_page*)))
    PROVIDE(__stop_mbr_params_page = .);
  } > mbr_params_page
  .uicr_mbr_params_page :
  {
    PROVIDE(__start_uicr_mbr_params_page = .);
    KEEP(*(SORT(.uicr_mbr_params_page*)))
    PROVIDE(__stop_uicr_mbr_params_page = .);
  } > uicr_mbr_params_page
}

INCLUDE "nrf52.ld"
