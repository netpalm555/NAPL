; ---------------------------------------------------------------------------
; Tell compiler to generate 64 bit code
; ---------------------------------------------------------------------------
bits 64

; ---------------------------------------------------------------------------
; Data segment:
; ---------------------------------------------------------------------------
section .data use64

        ; Multiline printf format
        txt_format: db 10,"==========================="
                        db 10,"Hello world:"
                        db 10,"==========================="
                        db 10,"param1: %u"
                        db 10,"param2: %I64u"
                        db 10,"param3: %f" 
                        db 10,"param4: 0x%X"
                        db 10,"param5: %f"
                        db 10,"param6: 0x%I64X"
                        db 10,"===========================",10,0

        align 16 ; Align txt_format data to 16 byte boundary

        ; Parameters:
        param1: dq 0xFFFFFFFF
        param2: dq 0xFFFFFFFFFFFFFFFF
        param3: dq 29999.452
        param4: dq 0xDEADBEEF ; Some other value
        param5: dq 555595.5477
        param6: dq 0xABBABABAABBABABA

; ---------------------------------------------------------------------------
; Code segment:
; ---------------------------------------------------------------------------
section .text use64

        ; ---------------------------------------------------------------------------
        ; Define macro: Invoke
        ; ---------------------------------------------------------------------------
        %macro Invoke 1-*
                %if %0 > 1
                        %rotate 1
                        mov rcx,qword %1
                        %rotate 1
                        %if %0 > 2
                                mov rdx,qword %1
                                %rotate 1
                                %if  %0 > 3
                                        mov r8,qword %1
                                        %rotate 1
                                        %if  %0 > 4
                                                mov r9,qword %1
                                                %rotate 1
                                                %if  %0 > 5
                                                        %assign max %0-5
                                                        %assign i 32
                                                        %rep max
                                                                mov rax,qword %1
                                                                mov qword [rsp+i],rax
                                                                %assign i i+8
                                                                %rotate 1
                                                        %endrep
                                                %endif
                                        %endif
                                %endif
                        %endif
                %endif
                ; ------------------------
                ; call %1 ; would be the same as this:
                ; -----------------------------------------
                sub rsp,qword 8
                mov qword [rsp],%%returnAddress
                jmp %1
                %%returnAddress:
                ; -----------------------------------------
        %endmacro

        ; ---------------------------------------------------------------------------
        ; C management
        ; ---------------------------------------------------------------------------
        global main
        extern printf
	
main:
        ; -----------------------------------------------------------------------------
        ; Allocate stack memory
        ; -----------------------------------------------------------------------------
        sub rsp,8*7
        ; Allocated 8*7 bytes:
        ; 8*4 from them are defaut parameters for all functions.
        ; 8*3 from them are those extra three parameters on stack.
        ; Total allocated space for seven parameters.
        ; 4x Default parameters are passed via registers.
        ; Those 3x extra parameters are passed via stack.
        ; !!! Always allocate stack space => odd number * 8, 
        ; like now is just like we need => 8*7, doing like this will
        ; balance stack and make it aligned on 16 byte boundary.
        
        ; -----------------------------------------------------------------------------
        ; Call printf with seven parameters
        ; 4x of them are assigned to registers.
        ; 3x of them are assigned to stack spaces.
        ; -----------------------------------------------------------------------------
        ; Call printf with seven parameters
        ; -----------------------------------------------------------------------------
        Invoke printf,txt_format,[param1],[param2],[param3],[param4],[param5],[param6]

        ; -----------------------------------------------------------------------------
        ; The same, BUT, without 'Invoke' macro would be like this:
        ; -----------------------------------------------------------------------------
        ; #3x# ; Stack parameters
        mov rax,qword [param6]
        mov qword [rsp+48],rax
        mov rax,qword [param5]
        mov qword [rsp+40],rax
        mov rax,qword [param4]
        mov qword [rsp+32],rax
        ; ---------------------------
        ; #4x# ; Register parameters
        mov r9,qword [param3]
        mov r8,qword [param2]
        mov rdx,qword [param1]
        mov rcx,qword txt_format
        ; ---------------------------
        call printf
        ; ---------------------------

        ; -----------------------------------------------------------------------------
        ; Release stack memory
        ; -----------------------------------------------------------------------------
        add rsp,8*7

        ; -----------------------------------------------------------------------------
        ; Quit
        ; -----------------------------------------------------------------------------
        mov rax,qword 0
        ret

; ----
; END ----
; ----