format PE Console 4.0

include 'C:\fasm\INCLUDE\win32a.inc'

entry start

section '.data' data readable writeable
        bufferout db 'HELLO WORLD!',10,0
section '.idata' import data readable
        library\
                kernel32,'kernel32.dll'
        import kernel32,\
                GetStdHandle,'GetStdHandle',\
                WriteConsoleA,'WriteConsoleA',\
                ExitProcess,'ExitProcess'
section '.bss' readable writeable
        byteswritten dd ?
section '.code' code readable executable
start:
        invoke GetStdHandle,STD_OUTPUT_HANDLE
        mov ebp, eax
        invoke WriteConsoleA,ebp,bufferout,13,byteswritten,0
        invoke ExitProcess,0
