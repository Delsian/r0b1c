set(TARGET_PLATFORM "NRF52" CACHE STRING "Target platform")
#SET(CMAKE_ECLIPSE_VERSION 4.11) 

# Toolchain location
if (NOT "$ENV{TOOLCHAIN_DIR}" STREQUAL "")
	set(TOOLCHAIN_DIR "$ENV{TOOLCHAIN_DIR}" CACHE INTERNAL "TOOLCHAIN_DIR - Copied from environment variable")
else()
	message("var TOOLCHAIN_DIR not set, use default")
	set(TOOLCHAIN_DIR /usr)
endif()

set(CMAKE_MAKE_PROGRAM make CACHE FILEPATH "" FORCE)
SET(TOOLCHAIN_NAME arm-none-eabi)
SET(TOOLCHAIN_BIN_DIR ${TOOLCHAIN_DIR}/bin)
SET(TOOLCHAIN_LIBC_DIR ${TOOLCHAIN_DIR}/${TOOLCHAIN_NAME}/lib)
SET(TOOLCHAIN_INC_DIR ${TOOLCHAIN_LIBC_DIR}/include)
SET(TOOLCHAIN_LIB_DIR ${TOOLCHAIN_LIBC_DIR}/usr/lib)

set(CMAKE_TRY_COMPILE_TARGET_TYPE STATIC_LIBRARY)

set(OBJCOPY ${TOOLCHAIN_NAME}-objcopy)
set(MERGEHEX mergehex)

set(CMAKE_C_COMPILER_WORKS 1)
set(CMAKE_CXX_COMPILER_WORKS 1)

set(CMAKE_ASM_COMPILER ${TOOLCHAIN_NAME}-gcc)
set(CMAKE_C_COMPILER ${TOOLCHAIN_NAME}-gcc)
set(CMAKE_CXX_COMPILER ${TOOLCHAIN_NAME}-g++)

# SDK dirs
SET(SDK_DIR ${CMAKE_SOURCE_DIR}/../SDK)
SET(SDK_LIB_DIR ${SDK_DIR}/components/libraries/)
SET(SDK_LOG_DIR ${SDK_DIR}/components/libraries/log/src/)
SET(SDK_BLE_DIR ${SDK_DIR}/components/ble/)
SET(SDK_MOD_DIR ${SDK_DIR}/modules/nrfx/)
SET(SDK_SD_DIR ${SDK_DIR}/components/softdevice/s132/)

SET(CMAKE_SYSTEM_NAME Linux CACHE INTERNAL "system name")
SET(CMAKE_SYSTEM_PROCESSOR arm CACHE INTERNAL "processor")

# linker script
IF (CMAKE_PROJECT_NAME STREQUAL "bootloader")
	SET(LINKER_SCRIPT bootloader.ld)
ELSE()
	SET(LINKER_SCRIPT controller.ld)
ENDIF()

SET(CMAKE_CXX_FLAGS
    "-mthumb -mcpu=cortex-m4 \
    -fno-builtin -fomit-frame-pointer \
    -ffunction-sections -fdata-sections -flto \
    -fno-strict-aliasing -fshort-enums \
    -Wall -Wno-pointer-sign -mabi=aapcs \
    -mfpu=fpv4-sp-d16 -mfloat-abi=hard \
    -O0 -g3")
SET(CMAKE_C_FLAGS
    "${CMAKE_CXX_FLAGS}")
SET(CMAKE_ASM_FLAGS 
    "-MP -MD -x assembler-with-cpp")
set(CMAKE_EXE_LINKER_FLAGS
    "-mthumb -mabi=aapcs -mfpu=fpv4-sp-d16 \
    -std=gnu++98 \
    -L ${SDK_MOD_DIR}mdk -L ../config \
    -T${LINKER_SCRIPT} -Wl,--gc-sections \
    -Wl,-Map=${CMAKE_PROJECT_NAME}.map \
    --specs=nano.specs")

# Startup
SET(NRF52_STARTUP ${SDK_DIR}/modules/nrfx/mdk/gcc_startup_nrf52.S)

# CMSIS
SET(NRF52_SYSTEM ${SDK_DIR}/modules/nrfx/mdk/system_nrf52.c)

# Softdevice
SET(SOFTDEVICE_PATH "${SDK_SD_DIR}hex/s132_nrf52_6.1.1_softdevice.hex")

# Utilities
if (NOT "$ENV{NRFJPROG_DIR}" STREQUAL "")
	set(NRFJPROG_DIR "$ENV{NRFJPROG_DIR}" CACHE INTERNAL "NRFJPROG_DIR - Copied from environment variable")
else()
	message("var NRFJPROG_DIR not set, use default")
	set(NRFJPROG_DIR /opt/nrfjprog/nrfjprog)
endif()
SET(NRFJPROG ${NRFJPROG_DIR}/nrfjprog)
SET(NRFUTIL ${SDK_DIR}/external_tools/nrfutil.exe)

# Bootloader settings
SET(SETTINGS_HEX "../config/settings.hex")


