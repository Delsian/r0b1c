
SET(SDK_CONFIG_FILE ${SDK_DIR}/../config/sdk_config.h)
SET(SDK_CONFIG_TOOL ${SDK_DIR}/external_tools/cmsisconfig/CMSIS_Configuration_Wizard.jar)

SET(SDK_LIB_INCLUDE_DIRS
    ${SDK_LIB_DIR}atomic
    ${SDK_LIB_DIR}atomic_fifo
    ${SDK_LIB_DIR}atomic_flags
    ${SDK_LIB_DIR}balloc
    ${SDK_LIB_DIR}crc32
    ${SDK_LIB_DIR}crc16
    ${SDK_LIB_DIR}csense
    ${SDK_LIB_DIR}csense_drv
    ${SDK_LIB_DIR}delay
    ${SDK_LIB_DIR}ecc
    ${SDK_LIB_DIR}experimental_section_vars
    ${SDK_LIB_DIR}experimental_task_manager
    ${SDK_LIB_DIR}fds
    ${SDK_LIB_DIR}fstorage
    ${SDK_LIB_DIR}hardfault
    ${SDK_LIB_DIR}log
    ${SDK_LIB_DIR}log/src
    ${SDK_LIB_DIR}low_power_pwm
    ${SDK_LIB_DIR}memobj
    ${SDK_LIB_DIR}mem_manager
    ${SDK_LIB_DIR}mpu
    ${SDK_LIB_DIR}mutex
    ${SDK_LIB_DIR}pwr_mgmt
    ${SDK_LIB_DIR}queue
    ${SDK_LIB_DIR}ringbuf
    ${SDK_LIB_DIR}scheduler
    ${SDK_LIB_DIR}sortlist
    ${SDK_LIB_DIR}stack_guard
    ${SDK_LIB_DIR}stack_info
    ${SDK_LIB_DIR}strerror
    ${SDK_LIB_DIR}svc
    ${SDK_LIB_DIR}timer
    ${SDK_LIB_DIR}twi_mngr
    ${SDK_LIB_DIR}util
	)

SET(SDK_BOOT_INCLUDE_DIRS
    ${SDK_LIB_DIR}bootloader
    ${SDK_LIB_DIR}bootloader/ble_dfu
    ${SDK_LIB_DIR}bootloader/dfu
	)
	
SET(SDK_CRYPTO_INCLUDE_DIRS
    ${SDK_LIB_DIR}crypto
    ${SDK_LIB_DIR}crypto/backend/oberon
    ${SDK_LIB_DIR}crypto/backend/optiga
    ${SDK_LIB_DIR}crypto/backend/cifra
    ${SDK_LIB_DIR}crypto/backend/cc310
    ${SDK_LIB_DIR}crypto/backend/cc310_bl
    ${SDK_LIB_DIR}crypto/backend/nrf_hw
    ${SDK_LIB_DIR}crypto/backend/nrf_sw
    ${SDK_LIB_DIR}crypto/backend/mbedtls
    ${SDK_LIB_DIR}crypto/backend/micro_ecc
    ${SDK_LIB_DIR}sha256
    ${SDK_DIR}/external/cifra_AES128-EAX
    ${SDK_DIR}/external/mbedtls/include
    ${SDK_DIR}/external/micro-ecc/micro-ecc
    ${SDK_DIR}/external/nrf_cc310/include
    ${SDK_DIR}/external/nrf_oberon
    ${SDK_DIR}/external/nrf_oberon/include
    ${SDK_DIR}/external/nrf_tls/mbedtls/nrf_crypto/config
	)

SET(SDK_BLE_INCLUDE_DIRS
	${SDK_BLE_DIR}ble_advertising
	${SDK_BLE_DIR}ble_db_discovery
	${SDK_BLE_DIR}ble_link_ctx_manager
	${SDK_BLE_DIR}ble_services/ble_bas
	${SDK_BLE_DIR}ble_services/ble_dfu
	${SDK_BLE_DIR}ble_services/ble_dis
	${SDK_BLE_DIR}ble_services/ble_escs
	${SDK_BLE_DIR}ble_services/ble_nus
	${SDK_BLE_DIR}common
	${SDK_BLE_DIR}nrf_ble_gatt
	${SDK_BLE_DIR}nrf_ble_qwr
	${SDK_BLE_DIR}peer_manager
	)

SET(SDK_INCLUDE_DIRS
    ${SDK_DIR}/components/toolchain/cmsis/include
    ${SDK_DIR}/components/boards
    ${SDK_DIR}/external/segger_rtt
    ${SDK_DIR}/external/fprintf
    ${SDK_DIR}/external/nano-pb
    ${SDK_DIR}/external/utf_converter
    ${SDK_DIR}/integration/nrfx/legacy
    ${SDK_MOD_DIR}
    ${SDK_MOD_DIR}drivers
    ${SDK_MOD_DIR}drivers/include
    ${SDK_MOD_DIR}hal
    ${SDK_MOD_DIR}mdk
    ${SDK_SD_DIR}headers
    ${SDK_SD_DIR}headers/nrf52
    ${SDK_SD_DIR}../common
	)

SET(SDK_LOG_SOURCES
    ${SDK_LOG_DIR}nrf_log_backend_rtt.c
    ${SDK_LOG_DIR}nrf_log_backend_serial.c
    ${SDK_LOG_DIR}nrf_log_default_backends.c
    ${SDK_LOG_DIR}nrf_log_frontend.c
    ${SDK_LOG_DIR}nrf_log_str_formatter.c
    ${SDK_DIR}/external/segger_rtt/SEGGER_RTT.c
    ${SDK_DIR}/external/segger_rtt/SEGGER_RTT_printf.c
    ${SDK_DIR}/external/segger_rtt/SEGGER_RTT_Syscalls_GCC.c
    )

SET(SDK_LIB_SOURCES
    ${SDK_LIB_DIR}atomic/nrf_atomic.c
    ${SDK_LIB_DIR}atomic_flags/nrf_atflags.c
    ${SDK_LIB_DIR}atomic_fifo/nrf_atfifo.c
    ${SDK_LIB_DIR}balloc/nrf_balloc.c
    ${SDK_LIB_DIR}crc16/crc16.c
    ${SDK_LIB_DIR}crc32/crc32.c
    ${SDK_LIB_DIR}experimental_section_vars/nrf_section_iter.c
    ${SDK_LIB_DIR}fds/fds.c
    ${SDK_LIB_DIR}fstorage/nrf_fstorage.c
    ${SDK_LIB_DIR}fstorage/nrf_fstorage_nvmc.c
    ${SDK_LIB_DIR}fstorage/nrf_fstorage_sd.c
    ${SDK_LIB_DIR}hardfault/hardfault_implementation.c
    ${SDK_LIB_DIR}hardfault/nrf52/handler/hardfault_handler_gcc.c
    ${SDK_LIB_DIR}memobj/nrf_memobj.c
    ${SDK_LIB_DIR}mem_manager/mem_manager.c
    ${SDK_LIB_DIR}pwr_mgmt/nrf_pwr_mgmt.c
    ${SDK_LIB_DIR}queue/nrf_queue.c
    ${SDK_LIB_DIR}ringbuf/nrf_ringbuf.c
    ${SDK_LIB_DIR}scheduler/app_scheduler.c
    ${SDK_LIB_DIR}strerror/nrf_strerror.c
    ${SDK_LIB_DIR}svc/nrf_svc_handler.c
    ${SDK_LIB_DIR}timer/app_timer.c
    ${SDK_LIB_DIR}twi_mngr/nrf_twi_mngr.c
    ${SDK_LIB_DIR}util/app_error.c
    ${SDK_LIB_DIR}util/app_error_weak.c
    ${SDK_LIB_DIR}util/app_error_handler_gcc.c
    ${SDK_LIB_DIR}util/nrf_assert.c
    ${SDK_LIB_DIR}util/app_util_platform.c
    ${SDK_MOD_DIR}hal/nrf_nvmc.c
    ${SDK_DIR}/external/utf_converter/utf.c
    ${SDK_DIR}/integration/nrfx/legacy/nrf_drv_rng.c
    ${SDK_DIR}/integration/nrfx/legacy/nrf_drv_twi.c
    )

SET(SDK_BOOT_SOURCES
    ${SDK_LIB_DIR}bootloader/nrf_bootloader.c
    ${SDK_LIB_DIR}bootloader/nrf_bootloader_app_start.c
    ${SDK_LIB_DIR}bootloader/nrf_bootloader_app_start_final.c
    ${SDK_LIB_DIR}bootloader/nrf_bootloader_dfu_timers.c
    ${SDK_LIB_DIR}bootloader/nrf_bootloader_fw_activation.c
    ${SDK_LIB_DIR}bootloader/nrf_bootloader_info.c
    ${SDK_LIB_DIR}bootloader/nrf_bootloader_wdt.c
    ${SDK_LIB_DIR}bootloader/ble_dfu/nrf_dfu_ble.c
    ${SDK_LIB_DIR}bootloader/dfu/dfu-cc.pb.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_flash.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_handling_error.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_mbr.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_req_handler.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_settings.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_transport.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_utils.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_validation.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_ver_validation.c
	)

SET(SDK_CRYPTO_SOURCES
    ${SDK_LIB_DIR}crypto/nrf_crypto_aead.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_aes.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_aes_shared.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_ecc.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_ecdh.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_ecdsa.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_eddsa.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_error.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_init.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_hash.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_hmac.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_hkdf.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_rng.c
    ${SDK_LIB_DIR}crypto/nrf_crypto_shared.c
    ${SDK_LIB_DIR}/crypto/backend/cifra/cifra_backend_aes_aead.c
    ${SDK_LIB_DIR}crypto/backend/mbedtls/mbedtls_backend_aes.c
    ${SDK_LIB_DIR}crypto/backend/mbedtls/mbedtls_backend_ecc.c
    ${SDK_LIB_DIR}crypto/backend/mbedtls/mbedtls_backend_aes_aead.c
    ${SDK_LIB_DIR}crypto/backend/mbedtls/mbedtls_backend_ecdh.c
    ${SDK_LIB_DIR}crypto/backend/mbedtls/mbedtls_backend_ecdsa.c
    ${SDK_LIB_DIR}crypto/backend/mbedtls/mbedtls_backend_hash.c
    ${SDK_LIB_DIR}crypto/backend/mbedtls/mbedtls_backend_hmac.c
    ${SDK_LIB_DIR}crypto/backend/mbedtls/mbedtls_backend_init.c
    ${SDK_LIB_DIR}crypto/backend/micro_ecc/micro_ecc_backend_ecc.c
    ${SDK_LIB_DIR}crypto/backend/micro_ecc/micro_ecc_backend_ecdh.c
    ${SDK_LIB_DIR}crypto/backend/micro_ecc/micro_ecc_backend_ecdsa.c
    ${SDK_LIB_DIR}crypto/backend/nrf_sw/nrf_sw_backend_hash.c
    ${SDK_LIB_DIR}crypto/backend/nrf_hw/nrf_hw_backend_init.c
	${SDK_LIB_DIR}crypto/backend/nrf_hw/nrf_hw_backend_rng.c
	${SDK_LIB_DIR}crypto/backend/nrf_hw/nrf_hw_backend_rng_mbedtls.c
    ${SDK_LIB_DIR}crypto/backend/oberon/oberon_backend_ecc.c
    ${SDK_LIB_DIR}crypto/backend/oberon/oberon_backend_hash.c
    ${SDK_LIB_DIR}crypto/backend/oberon/oberon_backend_chacha_poly_aead.c
	${SDK_LIB_DIR}crypto/backend/oberon/oberon_backend_ecdh.c
	${SDK_LIB_DIR}crypto/backend/oberon/oberon_backend_ecdsa.c
	${SDK_LIB_DIR}crypto/backend/oberon/oberon_backend_eddsa.c
	${SDK_LIB_DIR}crypto/backend/oberon/oberon_backend_hmac.c
    ${SDK_LIB_DIR}ecc/ecc.c
    ${SDK_LIB_DIR}sha256/sha256.c
   	${SDK_DIR}/external/nano-pb/pb_common.c
    ${SDK_DIR}/external/nano-pb/pb_decode.c
    ${SDK_DIR}/external/mbedtls/library/aes.c
    ${SDK_DIR}/external/mbedtls/library/aesni.c
    ${SDK_DIR}/external/mbedtls/library/arc4.c
    ${SDK_DIR}/external/mbedtls/library/asn1parse.c
    ${SDK_DIR}/external/mbedtls/library/base64.c
    ${SDK_DIR}/external/mbedtls/library/bignum.c
    ${SDK_DIR}/external/mbedtls/library/blowfish.c
    ${SDK_DIR}/external/mbedtls/library/camellia.c
    ${SDK_DIR}/external/mbedtls/library/ccm.c
    ${SDK_DIR}/external/mbedtls/library/certs.c
    ${SDK_DIR}/external/mbedtls/library/cipher.c
    ${SDK_DIR}/external/mbedtls/library/cipher_wrap.c
    ${SDK_DIR}/external/mbedtls/library/cmac.c
    ${SDK_DIR}/external/mbedtls/library/ctr_drbg.c
    ${SDK_DIR}/external/mbedtls/library/debug.c
    ${SDK_DIR}/external/mbedtls/library/des.c
    ${SDK_DIR}/external/mbedtls/library/dhm.c
    ${SDK_DIR}/external/mbedtls/library/ecdh.c
    ${SDK_DIR}/external/mbedtls/library/ecdsa.c
    ${SDK_DIR}/external/mbedtls/library/ecp.c
    ${SDK_DIR}/external/mbedtls/library/ecp_curves.c
    ${SDK_DIR}/external/mbedtls/library/entropy.c
    ${SDK_DIR}/external/mbedtls/library/entropy_poll.c
    ${SDK_DIR}/external/mbedtls/library/error.c
    ${SDK_DIR}/external/mbedtls/library/gcm.c
    ${SDK_DIR}/external/mbedtls/library/havege.c
    ${SDK_DIR}/external/mbedtls/library/hmac_drbg.c
    ${SDK_DIR}/external/mbedtls/library/md.c
    ${SDK_DIR}/external/mbedtls/library/md2.c
    ${SDK_DIR}/external/mbedtls/library/md4.c
    ${SDK_DIR}/external/mbedtls/library/md5.c
    ${SDK_DIR}/external/mbedtls/library/md_wrap.c
    ${SDK_DIR}/external/mbedtls/library/memory_buffer_alloc.c
    ${SDK_DIR}/external/mbedtls/library/oid.c
    ${SDK_DIR}/external/mbedtls/library/padlock.c
    ${SDK_DIR}/external/mbedtls/library/pem.c
    ${SDK_DIR}/external/mbedtls/library/pk.c
    ${SDK_DIR}/external/mbedtls/library/pk_wrap.c
    ${SDK_DIR}/external/mbedtls/library/pkcs11.c
    ${SDK_DIR}/external/mbedtls/library/pkcs12.c
    ${SDK_DIR}/external/mbedtls/library/pkcs5.c
    ${SDK_DIR}/external/mbedtls/library/pkparse.c 
    ${SDK_DIR}/external/mbedtls/library/pkwrite.c
    ${SDK_DIR}/external/mbedtls/library/platform.c
    ${SDK_DIR}/external/mbedtls/library/ripemd160.c
    ${SDK_DIR}/external/mbedtls/library/rsa.c
    ${SDK_DIR}/external/mbedtls/library/sha1.c
    ${SDK_DIR}/external/mbedtls/library/sha256.c
    ${SDK_DIR}/external/mbedtls/library/sha512.c
    ${SDK_DIR}/external/mbedtls/library/ssl_cache.c
    ${SDK_DIR}/external/mbedtls/library/ssl_ciphersuites.c
    ${SDK_DIR}/external/mbedtls/library/ssl_cli.c
    ${SDK_DIR}/external/mbedtls/library/ssl_cookie.c
    ${SDK_DIR}/external/mbedtls/library/ssl_ticket.c
    ${SDK_DIR}/external/mbedtls/library/ssl_tls.c
    ${SDK_DIR}/external/mbedtls/library/threading.c
    ${SDK_DIR}/external/mbedtls/library/version.c
    ${SDK_DIR}/external/mbedtls/library/version_features.c
    ${SDK_DIR}/external/mbedtls/library/x509.c
    ${SDK_DIR}/external/mbedtls/library/x509_create.c
    ${SDK_DIR}/external/mbedtls/library/x509_crl.c
    ${SDK_DIR}/external/mbedtls/library/x509_crt.c
    ${SDK_DIR}/external/mbedtls/library/x509_csr.c
    ${SDK_DIR}/external/mbedtls/library/xtea.c
    ${SDK_DIR}/external/micro-ecc/micro-ecc/uECC.c
    ${SDK_DIR}/external/nrf_tls/mbedtls/replacements/asn1write.c
    ${SDK_DIR}/external/nrf_tls/mbedtls/replacements/ssl_srv.c
    ${SDK_DIR}/external/cifra_AES128-EAX/blockwise.c
    ${SDK_DIR}/external/cifra_AES128-EAX/cifra_cmac.c
	${SDK_DIR}/external/cifra_AES128-EAX/cifra_eax_aes.c
    ${SDK_DIR}/external/cifra_AES128-EAX/eax.c
    ${SDK_DIR}/external/cifra_AES128-EAX/gf128.c
    ${SDK_DIR}/external/cifra_AES128-EAX/modes.c
	)

SET(SDK_BLE_SOURCES
    ${SDK_BLE_DIR}ble_advertising/ble_advertising.c
    ${SDK_BLE_DIR}ble_link_ctx_manager/ble_link_ctx_manager.c
    ${SDK_BLE_DIR}common/ble_advdata.c
    ${SDK_BLE_DIR}common/ble_conn_params.c
    ${SDK_BLE_DIR}common/ble_conn_state.c
    ${SDK_BLE_DIR}common/ble_srv_common.c
    ${SDK_BLE_DIR}nrf_ble_gatt/nrf_ble_gatt.c
    ${SDK_BLE_DIR}nrf_ble_qwr/nrf_ble_qwr.c
    ${SDK_BLE_DIR}ble_services/ble_bas/ble_bas.c
    ${SDK_BLE_DIR}ble_services/ble_dfu/ble_dfu.c
    ${SDK_BLE_DIR}ble_services/ble_dfu/ble_dfu_bonded.c
    ${SDK_BLE_DIR}ble_services/ble_dfu/ble_dfu_unbonded.c
    ${SDK_BLE_DIR}ble_services/ble_dis/ble_dis.c
    ${SDK_BLE_DIR}ble_services/ble_nus/ble_nus.c
    ${SDK_SD_DIR}../common/nrf_sdh.c
    ${SDK_SD_DIR}../common/nrf_sdh_ble.c
    ${SDK_SD_DIR}../common/nrf_sdh_soc.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_svci.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_svci_handler.c
    ${SDK_LIB_DIR}bootloader/dfu/nrf_dfu_settings_svci.c
    )

AUX_SOURCE_DIRECTORY(${SDK_MOD_DIR}drivers/src/ SDK_MOD_DRV_SOURCES)
AUX_SOURCE_DIRECTORY(${SDK_DIR}/external/fprintf SDK_EXT_FPRINTF_SOURCES)
	
set(CMAKE_SHARED_LIBRARY_LINK_C_FLAGS "")
set(CMAKE_SHARED_LIBRARY_LINK_CXX_FLAGS "")

