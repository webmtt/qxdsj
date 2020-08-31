@echo off
@title 批处理的for循环
for %%i in (E:\pdf\*.pdf) do (
 "D:\Program Files\SWFTools\pdf2swf" -o E:\swf\%%~ni.swf -T -z -t %%i -s flashversion=9
)
pause