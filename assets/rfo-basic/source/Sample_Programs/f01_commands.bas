 There is 100+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 41
!! - Block Comment, 41
# - Format Line, 22
% - Middle of Line Comment, 42
ABS(<nexp>), 45
ACOS(<nexp>), 47
Array. reverse Array$[{<start>,<length>}], 33
Array. reverse Array[{<start>,<length>}], 33
Array.average <Average_nvar>, Array[{<start>,<length>}], 32
Array.copy SourceArray$[{<start>,<length>}], DestinationArray$[{{-}<extras>}], 32
Array.copy SourceArray[{<start>,<length>}], DestinationArray[{{-}<extras>}], 32
Array.delete Array[], Array$[] ..., 33
Array.length n, Array$[{<start>,<length>}], 33
Array.length n, Array[{<start>,<length>}], 33
Array.load Array$[], <sexp>, <sexp>, ... }, 33
Array.load Array[], <nexp>, <nexp>, ...}, 33
Array.max <max_nvar> Array[{<start>,<length>}], 33
Array.min <min_nvar>, Array[{<start>,<length>}], 33
Array.search Array$[{<start>,<length>}], <value_sexp>, <result_nvar>{,<start_nexp>}, 33
Array.search Array[{<start>,<length>}], <value_nexp>, <result_nvar>{,<start_nexp>}, 33
Array.shuffle Array[{<start>,<length>}], 34
Array.sort Array[{<start>,<length>}], 34
Array.std_dev <sd_nvar>, Array[{<start>,<length>}], 34
Array.sum <sum_nvar>, Array[{<start>,<length>}], 34
Array.variance <v_nvar>, Array[{<start>,<length>}], 34
ASCII(<sexp>), 48
ASIN(<nexp>), 47
ATAN(<nexp>), 47
ATAN2 (<nexp_x>, <nexp_y>), 47
Audio.isdone <lvar>, 122
Audio.length <length_nvar>, <aft_nexp>, 122
Audio.load  <aft_nvar>, <filename_sexp>, 121
Audio.loop, 122
Audio.pause, 122
Audio.play <aft_nexp>, 121
Audio.position.current  <nvar>, 122
Audio.position.seek <nexp>, 122
Audio.record.start <fn_svar>, 123
Audio.record.stop, 123
Audio.release <aft_nexp>, 122
Audio.stop, 121
Audio.volume <left_nexp>, <right_nexp>, 122
Back.resume, 60
Background(), 49
Background.Resume, 98
BAND(<nexp1>, <nexp2>), 44
BIN$(<nexp>), 51
BIN(<sexp>), 47
BOR(<nexp1>, <nexp2>), 44
Browse <url_sexp>, 87
Bt.close, 85
Bt.connect {0|1}, 85
Bt.device.name <svar>, 86
Bt.disconnect, 85
Bt.onReadReady.Resume, 86
Bt.open {0|1}, 84
Bt.read.bytes <svar>, 86
Bt.read.ready <nvar>, 86
Bt.reconnect, 85
Bt.set.uuid <sexp>, 86
Bt.status <nvar>, 85
Bt.write <sexp>|<nexp> {,|;} . . . . <sexp>|<nexp>{,|;}, 85
Bundle.clear <pointer_nexp>, 39
Bundle.contain  <pointer_nexp>, <key_sexp> , <contains_nvar>, 39
Bundle.create <pointer_nvar>, 38
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 39
Bundle.keys <pointer_nexp>, <list_nvar>, 39
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 38
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 39
BXOR(<nexp1>, <nexp2>), 45
Byte.close <file_table_nvar>, 74
Byte.copy <file_table_nvar>,<output_file_svar>, 75
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 74
Byte.position.get  <file_table_nvar>, <position_nexp>, 75
Byte.position.set <file_table_nvar>, <position_nexp>, 75
Byte.read.buffer <file_table_nvar>, <count_nexp>, <buffer_svar>, 75
Byte.read.byte <file_table_nvar>, <byte_nvar>, 74
Byte.write.buffer <file_table_nvar>, <sexp>, 75
Byte.write.byte <file_table_nvar>, <byte_nexp>|<sexp>, 75
Call <user_defined_function>, 54
CBRT(<nexp>), 45
CEIL(<nexp>), 45
CHR$ (<nexp>), 50
Clipboard.get <svar>, 87
Clipboard.put <sexp>, 87
Clock(), 49
CLS, 66
Console.line.text <line_nexp>, <text_svar>, 66
Console.line.touched <line_nvar> {, <press_lvar>}, 66
Console.save <filename_sexp>, 67
Console.Title { <title_sexp>}, 65
ConsoleTouch.Resume, 60
COS(<nexp>), 46
COSH(<nexp>), 46
D_U.break, 57
D_U.continue, 57
Debug.dump.array Array[], 63
Debug.dump.bundle  <bundlePtr_nexp>, 63
Debug.dump.list  <listPtr_nexp>, 63
Debug.dump.scalars, 63
Debug.dump.stack  <stackPtr_nexp>, 63
Debug.echo.off, 62
Debug.echo.on, 62
Debug.off, 62
Debug.on, 62
Debug.print, 63
Debug.show, 65
Debug.show.array Array[], 63
Debug.show.bundle  <bundlePtr_nexp>, 64
Debug.show.list  <listPtr_nexp>, 64
Debug.show.program, 65
Debug.show.scalars, 63
Debug.show.stack  <stackPtr_nexp>, 64
Debug.show.watch, 64
Debug.watch var, var, �, var, 64
Decrypt  <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 88
Device <svar>, 91
Dim[Array n, n, ...], Array$[n, n, ...] ..., 32
Do - Until <lexp>, 56
Echo.off, 87
Echo.on, 87
Email.send <recipient_sxep>, <subject_sexp>, <body_sexp>, 97
Encrypt <pw_sexp>, <source_sexp>, <encrypted_svar>, 88
End{ <msg_sexp>}, 61
Ends_with ( <Look_for_sexp>, <look_in_sexp>), 48
Exit, 61
EXP(<nexp>), 46
F_N.break, 56
F_N.continue, 56
File.delete <lvar>, <path_sexp>, 70
File.dir <path_sexp>, Array[], 70
File.exists <lvar>, <path_sexp>, 71
File.mkdir <path_sexp>, 71
File.rename <Old_Path_sexp>, <New_Path_sexp>, 71
File.root <svar>, 71
File.size <size_nvar>, <path_sexp>, 71
FLOOR(<nexp>), 46
Fn.def  name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 53
Fn.end, 54
Fn.rtn <sexp>|<nexp>, 54
For - To - Step - Next, 55
FORMAT$(<pattern_sexp>, <nexp>), 51
Ftp.cd <new_directory_sexp>, 83
Ftp.close, 82
Ftp.delete <filename_sexp>, 83
Ftp.dir <list_nvar>, 83
Ftp.get <source_sexp>, <destination_sexp>, 83
Ftp.mkdir <directory_sexp>, 83
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 82
Ftp.put <source_sexp>, <destination_sexp>, 82
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 83
Ftp.rmdir <directory_sexp>, 83
GETERROR$(), 50
GoSub <label>, Return, 57
GoTo <label>, 58
Gps.accuracy <nvar>, 125
Gps.altitude <nvar>, 126
Gps.bearing <nvar>, 126
Gps.close, 125
Gps.latitude <nvar>, 125
Gps.longitude <nvar>, 125
Gps.open, 125
Gps.provider <svar>, 125
Gps.speed <nvar>, 126
Gps.time <nvar>, 126
Gr.arc <object_number_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 107
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 112
Gr.bitmap.crop <new_bitmap_object_nvar>, <source_bitmap_object_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 113
Gr.bitmap.delete <bitmap_ptr_nvar>, 113
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nvar>, x , y, 113
Gr.bitmap.drawinto.end, 114
Gr.bitmap.drawinto.start <bitmap_ptr_nvar>, 114
gr.bitmap.load bitmap_ptr, File_name$, 112
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 113
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nvar>, width, height {, <smoothing_lvar> }, 112
Gr.bitmap.size <bitmap_ptr_nvar>, width, height, 112
Gr.bounded.touch touched, left, top, right, bottom, 110
Gr.bounded.touch2 touched, left, top, right, bottom, 110
Gr.brightness <nexp>, 106
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 116
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 116
Gr.camera.select 1|2, 115
Gr.camera.shoot <bm_ptr_nvar>, 115
Gr.circle <object_number_nvar>, x, y, radius, 107
Gr.clip <object__ptr_nvar>,  <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 119
Gr.close, 105
Gr.cls, 105
Gr.color alpha, red, green, blue, <style_nexp>, 103
Gr.front  flag, 106
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 113
Gr.get.params <object_ptr_nvar>, <param_array$[]>, 117
Gr.get.pixel x, y, alpha, red, green, blue, 116
Gr.get.position <object_ptr_nvar>,  x, y, 117
Gr.get.textbounds <sexp>, left, top, right, bottom, 111
Gr.get.type <object_ptr_nvar>, <type_svar>, 117
Gr.get.value <object_ptr_nvar>, <tag_sexp>, {<value_nvar | value_svar>}, 117
Gr.GetDL <dl_array[]> {, <keep_all_objects_lexp> }, 120
Gr.hide <object_number_nvar>, 109
Gr.line <object_number_nvar>, x1, y1, x2, y2, 107
Gr.modify <object_ptr_nvar>,  <tag_sexp>, {<value_nvar | value_svar>}, 117
Gr.NewDL <dl_array[{<start>,<length>}]>, 120
Gr.onGRTouch.resume, 110
Gr.open alpha, red, green, blue {, <ShowStatusBar_lexp> {, <orientation_nexp>}}, 103
Gr.orientation <nexp>, 104
Gr.oval <object_number_nvar>, left, top, right, bottom, 107
Gr.paint.get <object_ptr_nvar>, 118
Gr.point <object_number_nvar>, x, y, 106
Gr.poly <object_number_nvar>, list_pointer {,x,y}, 108
Gr.rect <object_number_nvar>, left, top, right, bottom, 107
Gr.render, 104
Gr.rotate.end {<obj_nvar>}, 114
Gr.rotate.start angle, x, y{,<obj_nvar>}, 114
Gr.save <filename_sexp> {,<quality_nexp>}, 116
Gr.scale x_factor, y_factor, 105
Gr.screen width, height{, density }, 105
Gr.screen.to_bitmap <bm_ptr_nvar>, 116
Gr.set.AntiAlias <lexp>, 104
Gr.set.pixels <object_number_nvar>, pixels[{<start>,<length>}] {,x,y}, 108
Gr.set.stroke <nexp>, 104
Gr.show <object_number_nvar>, 109
Gr.StatusBar.Show  <nexp>, 104
Gr.text.align type, 110
Gr.text.bold <lexp>, 111
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 112
Gr.text.size <nexp>, 110
Gr.text.skew <nexp>, 111
Gr.text.strike <lexp>, 111
Gr.text.typeface <nexp>, 111
Gr.text.underline <lexp>, 111
Gr.text.width <nvar>, <sexp>, 110
Gr.touch touched, x, y, 109
Gr.touch2 touched, x, y, 110
Gr_collision ( <object_1_nvar>, <object_2_nvar>), 48, 119
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 73
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 73
Headset <state_nvar>, <type_svar>, <mic_nvar>, 97
HEX$(<nexp>), 51
HEX(<sexp>), 47
Home, 98
Html.clear.cache, 78
Html.clear.history, 78
Html.close, 78
Html.get.datalink <data_svar>, 77
Html.go.back, 78
Html.go.forward, 78
Html.load.string <html_sexp>, 77
Html.load.url <file_sexp>, 76
Html.open {<show_status_bar_nexp>}, 76
Html.post <url_sexp>, <list_nexp>, 77
Http.post <url_sexp>, <list_nexp>, <result_svar>, 95
HYPOT(<nexp_x>, <nexp_y), 46
If  - Then - Else, 55
If  - Then - Else - Elseif - Endif, 54
Include FileNamePath, 91
Inkey$ <svar>, 67
Input <Prompt_sexp>, <nvar>|<svar>, {<Default_sexp>|<Default_nexp>}, 67
Is_In(<Search_for_sexp>, <Search_in_sexp>{, <start_nexp>}, 48
Kb.hide, 68
Kb.toggle, 68
Key.Resume, 61
LEFT$ (<sexp>, <nexp>), 50
LEN(<sexp>), 47
LET, 44
List.add <pointer_nexp>, <nexp>{,<nexp>...,<nexp>}, 36
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 36
List.add.array string_list_pointer, Array$[{<start>,<length>}], 36
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 36
List.clear <pointer_nexp>, 37
List.create N|S, <pointer_nvar>, 36
List.get <pointer_nexp>,<index_nexp>, <svar>|<nvar>, 37
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 37
List.remove <pointer_nexp>,<index_nexp>, 37
List.replace <pointer_nexp>,<index_nexp>, <sexp>|<nexp>, 36
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 37
List.size <pointer_nexp>, <nvar>, 37
List.ToArray <pointer_nexp>, Array$[] | Array[], 38
List.type <pointer_nexp>, <svar>, 37
LOG(<nexp>), 46
LOG10(<nexp>), 46
LOWER$(<sexp>), 51
MAX(<nexp>, <nexp>), 45
MenuKey.resume, 61
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 50
MIN(<nexp>, <nexp>), 45
MOD(<nexp1>, <nexp2>), 46
MyPhoneNumber <svar>, 96
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 97
OCT$(<nexp>, 51
OCT(<sexp>), 47
OnBackGround:, 98
OnBackKey:, 60
OnBtReadReady:, 86
OnConsoleTouch:, 60
OnError:, 59
OnGRTouch:, 110
OnKeyPress:, 61
OnMenuKey:, 61
OnTimer:, 90
Pause <ticks_nexp>, 91
Phone.call <sexp>, 96
Phone.rcv.init, 96
Phone.rcv.next  <state_nvar>, <number_svar>, 96
PI(), 46
Popup <message_sexp>, <x_nexp>, <y_nexp>, <duration_nexp>, 91
POW(<nexp1>, <nexp2>), 46
Print <sexp>|<nexp> {,|;} . . . . <sexp>|<nexp>{,|;}, 66
RANDOMIZE(<nexp>), 45
Read.data <number>|<string> {,<number>|<string>�,<number>|<string>}, 61
Read.from <nexp>, 62
Read.next  <svar>|<nvar>{,<svar>|<nvar>� , <svar>|<nvar>}, 62
REM, 41
REPLACE$( <target_sexp>, <argument_sexp>, <replace_sexp>), 50
RIGHT$(<sexp>, <nexp>), 50
RND(), 45
ROUND(<nexp>), 46
Run <filename_sexp> {, <data_sexp>}, 58
Select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 92
Sensors.close, 127
Sensors.list <sensor_array$[]>, 127
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 127
Sensors.read sensor_type, p1, p2, p3, 127
SHIFT (<value_nexp>, <bits_nexp>), 48
SIN(<nexp>), 46
SINH(<nexp>), 47
Sms.rcv.init, 96
Sms.rcv.next <svar>, 96
Sms.send <number_sexp>, <message_sexp>, 96
Socket.client.close, 80
Socket.client.connect <server_ip_sexp>, <port_nexp>, 79
Socket.client.read.file  <fw_nexp>, 80
Socket.client.read.line <line_svar>, 79
Socket.client.read.ready <nvar>, 80
Socket.client.server.ip <svar>, 79
Socket.client.status <status_nvar>, 79
Socket.client.write.bytes <sexp>, 80
Socket.client.write.file  <fr_nexp>, 80
Socket.client.write.line <line_sexp>, 80
Socket.myip  <svar>, 80
Socket.server.client.ip <nvar>, 82
Socket.server.close, 82
Socket.server.connect {<wait_lexp>}, 81
Socket.server.create <port_nexp>, 81
Socket.server.disconnect, 82
Socket.server.read.file <fw_nexp>, 82
Socket.server.read.line <svar>, 81
Socket.server.read.ready <nvar>, 81
Socket.server.status <status_nvar>, 81
Socket.server.write.bytes <sexp>, 81
Socket.server.write.file  <fr_nexp>, 82
Socket.server.write.line <sexp>, 81
Soundpool.load <soundID_nvar>, <file_path_sexp>, 124
Soundpool.open <MaxStreams_nexp>, 123
Soundpool.pause <streamID_nexp>, 125
Soundpool.play <streamID_nvar>, <sounded_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 124
Soundpool.release, 125
Soundpool.resume <streamID_nexp>, 125
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 124
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 124
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 124
Soundpool.stop <streamID_nexp>, 125
Soundpool.unload <soundID_nexp>, 124
Split <result_array$[]>, <source_sexp> {, <test_sexp>}, 92
Split.all <result_array$[]>, <source_sexp> {, <test_sexp>}, 92
Sql.close <DB_pointer_nvar>, 99
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 101
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 99
Sql.exec <DB_pointer_nvar>, <command_sexp>, 101
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN$, 99
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 99
Sql.next <done_lvar>, <cursor_nvar>, C1V$, C2V$, .., CNV$, 100
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 98
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp>, <order_sexp> } }, 100
Sql.query.length <length_nvar>, <cursor_nvar>, 100
Sql.query.position <position_nvar>, <cursor_nvar>, 100
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 101
Sql.update <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN$: <where_sexp>, 101
SQR(<nexp>), 45
Stack.clear <ptr_nexep>, 41
Stack.create N|S, <ptr_nvar>, 40
Stack.IsEmpty <ptr_nexep>, <nvar>, 41
Stack.peek <ptr_nexep>, <nvar>|<svar>, 40
Stack.pop <ptr_nexep>, <nvar>|<svar>, 40
Stack.push <ptr_nexep>, <nexp>|<sexp>, 40
Stack.type <ptr_nexep>, <svar>, 40
Starts_with (<Search_for_sexp>, <Search_in_sexp>{,<start_nexp>}, 48
STR$(<nexp>), 51
Stt.listen, 89
Stt.results <string_list_ptr_nvar>, 89
Su.close, 129
Su.open, 128
Su.read.line <svar>, 129
Su.read.ready <nvar>, 128
Su.write <sexp>, 128
Sw.begin <nexp>|<sexp>, 59
Sw.break, 59
Sw.case <nexp >|<sexp>, 59
Sw.default, 59
Sw.end, 59
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 87
System.close, 128
System.open, 128
System.read.line <svar>, 128
System.read.ready <nvar>, 128
System.write <sexp>, 128
TAN(<nexp>), 46
Text.close <file_table_nvar>, 72
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 68
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 72
Text.position.get  <file_table_nvar>, <position_nvar>, 73
Text.position.set <file_table_nvar>, <position_nexp>, 73
Text.readln <file_table_nvar>, <line_svar>, 72
Text.writeln <file_table_nexp>, <parms same as print>, 73
TGet <result_svar>, <prompt_sexp> {,  <title_sexp>}, 68
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 93
TIME(), 49
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 49
Timer.Clear, 91
Timer.Resume, 90
Timer.set <interval_nexp>, 90
TimeZone.get <tz_svar>, 94
TimeZone.list <tz_list_pointer_nvar>, 94
TimeZone.set { <tz_sexp> }, 94
TODEGREES(<nexp>), 47
Tone <frequency_nexp>, <duration_nexp>, 94
TORADIANS(<nexp>), 47
Tts.init, 88
Tts.speak  <sexp> {, <wait_lexp>}, 88
Tts.speak.toFile <sexp> {, <path_sexp>}, 88
Tts.stop, 88
UCODE(<sexp>), 48
UnDim Array[], Array$[], ..., 32
UPPER$(<sexp>), 51
VAL( <sexp> ), 47
VERSION$(), 51
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 94
W_R.break, 56
W_R.continue, 56
WakeLock <code_nexp>, 95
While <lexp> - Repeat, 56
WORD$(<source_sexp>, <n_nexp> {, <delimiter_sexp>}), 51