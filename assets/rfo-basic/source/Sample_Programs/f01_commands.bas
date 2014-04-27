 There is 100+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 42
!! - Block Comment, 42
# - Format Line, 22
% - Middle of Line Comment, 42
ABS(<nexp>), 45
ACOS(<nexp>), 48
Array. reverse Array$[{<start>,<length>}], 34
Array. reverse Array[{<start>,<length>}], 34
Array.average <Average_nvar>, Array[{<start>,<length>}], 32
Array.copy SourceArray$[{<start>,<length>}], DestinationArray$[{{-}<extras>}], 32
Array.copy SourceArray[{<start>,<length>}], DestinationArray[{{-}<extras>}], 32
Array.delete Array[], Array$[] ..., 33
Array.length n, Array$[{<start>,<length>}], 33
Array.length n, Array[{<start>,<length>}], 33
Array.load Array$[], <sexp>, <sexp>, ... }, 33
Array.load Array[], <nexp>, <nexp>, ...}, 33
Array.max <max_nvar> Array[{<start>,<length>}], 34
Array.min <min_nvar>, Array[{<start>,<length>}], 34
Array.search Array$[{<start>,<length>}], <value_sexp>, <result_nvar>{,<start_nexp>}, 34
Array.search Array[{<start>,<length>}], <value_nexp>, <result_nvar>{,<start_nexp>}, 34
Array.shuffle Array[{<start>,<length>}], 34
Array.sort Array[{<start>,<length>}], 34
Array.std_dev <sd_nvar>, Array[{<start>,<length>}], 34
Array.sum <sum_nvar>, Array[{<start>,<length>}], 34
Array.variance <v_nvar>, Array[{<start>,<length>}], 34
ASCII(<sexp>), 49
ASIN(<nexp>), 48
ATAN(<nexp>), 48
ATAN2 (<nexp_x>, <nexp_y>), 48
Audio.isdone <lvar>, 126
Audio.length <length_nvar>, <aft_nexp>, 125
Audio.load  <aft_nvar>, <filename_sexp>, 124
Audio.loop, 125
Audio.pause, 125
Audio.play <aft_nexp>, 124
Audio.position.current  <nvar>, 125
Audio.position.seek <nexp>, 125
Audio.record.start <fn_svar>, 126
Audio.record.stop, 126
Audio.release <aft_nexp>, 126
Audio.stop, 125
Audio.volume <left_nexp>, <right_nexp>, 125
Back.resume, 62
Background(), 50
Background.Resume, 101
BAND(<nexp1>, <nexp2>), 45
BIN$(<nexp>), 53
BIN(<sexp>), 49
BOR(<nexp1>, <nexp2>), 45
Browse <url_sexp>, 89
Bt.close, 87
Bt.connect {0|1}, 87
Bt.device.name <svar>, 89
Bt.disconnect, 88
Bt.onReadReady.Resume, 89
Bt.open {0|1}, 87
Bt.read.bytes <svar>, 89
Bt.read.ready <nvar>, 88
Bt.reconnect, 88
Bt.set.uuid <sexp>, 89
Bt.status <nvar>, 88
Bt.write <sexp>|<nexp> {,|;} ... <sexp>|<nexp>{,|;}, 88
Bundle.clear <pointer_nexp>, 40
Bundle.contain  <pointer_nexp>, <key_sexp> , <contains_nvar>, 40
Bundle.create <pointer_nvar>, 39
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 39
Bundle.keys <pointer_nexp>, <list_nvar>, 39
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 39
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 40
BXOR(<nexp1>, <nexp2>), 45
Byte.close <file_table_nvar>, 77
Byte.copy <file_table_nvar>,<output_file_svar>, 78
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 76
Byte.position.get  <file_table_nvar>, <position_nexp>, 78
Byte.position.set <file_table_nvar>, <position_nexp>, 78
Byte.read.buffer <file_table_nvar>, <count_nexp>, <buffer_svar>, 77
Byte.read.byte <file_table_nvar>, <byte_nvar>, 77
Byte.write.buffer <file_table_nvar>, <sexp>, 77
Byte.write.byte <file_table_nvar>, <byte_nexp>|<sexp>, 77
Call <user_defined_function>, 56
CBRT(<nexp>), 45
CEIL(<nexp>), 46
CHR$ (<nexp>), 51
Clipboard.get <svar>, 90
Clipboard.put <sexp>, 90
Clock(), 51
CLS, 68
Console.line.text <line_nexp>, <text_svar>, 69
Console.line.touched <line_nvar> {, <press_lvar>}, 69
Console.save <filename_sexp>, 69
Console.Title { <title_sexp>}, 67
ConsoleTouch.Resume, 62
COS(<nexp>), 48
COSH(<nexp>), 48
D_U.break, 59
D_U.continue, 59
Debug.dump.array Array[], 65
Debug.dump.bundle  <bundlePtr_nexp>, 65
Debug.dump.list  <listPtr_nexp>, 65
Debug.dump.scalars, 65
Debug.dump.stack  <stackPtr_nexp>, 65
Debug.echo.off, 64
Debug.echo.on, 64
Debug.off, 64
Debug.on, 64
Debug.print, 65
Debug.show, 67
Debug.show.array Array[], 65
Debug.show.bundle  <bundlePtr_nexp>, 66
Debug.show.list  <listPtr_nexp>, 66
Debug.show.program, 67
Debug.show.scalars, 65
Debug.show.stack  <stackPtr_nexp>, 66
Debug.show.watch, 66
Debug.watch var, var, ..., var, 66
Decrypt  <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 90
Device <svar>, 94
Dim Array [n, n, ...], Array$[n, n, ...] ..., 32
Do - Until <lexp>, 58
Echo.off, 90
Echo.on, 90
Email.send <recipient_sxep>, <subject_sexp>, <body_sexp>, 100
Encrypt <pw_sexp>, <source_sexp>, <encrypted_svar>, 90
End{ <msg_sexp>}, 63
Ends_with ( <Look_for_sexp>, <look_in_sexp>), 50
Exit, 63
EXP(<nexp>), 47
F_N.break, 58
F_N.continue, 58
File.delete <lvar>, <path_sexp>, 72
File.dir <path_sexp>, Array[], 73
File.exists <lvar>, <path_sexp>, 73
File.mkdir <path_sexp>, 73
File.rename <Old_Path_sexp>, <New_Path_sexp>, 73
File.root <svar>, 74
File.size <size_nvar>, <path_sexp>, 74
File.type <type_nvar>, <path_sexp>, 74
FLOOR(<nexp>), 46
Fn.def  name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 55
Fn.end, 56
Fn.rtn <sexp>|<nexp>, 56
For - To - Step - Next, 57
FORMAT$(<pattern_sexp>, <nexp>), 53
FRAC(<nexp>), 46
Ftp.cd <new_directory_sexp>, 86
Ftp.close, 85
Ftp.delete <filename_sexp>, 86
Ftp.dir <list_nvar>, 86
Ftp.get <source_sexp>, <destination_sexp>, 85
Ftp.mkdir <directory_sexp>, 86
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 85
Ftp.put <source_sexp>, <destination_sexp>, 85
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 86
Ftp.rmdir <directory_sexp>, 86
GETERROR$(), 51
GoSub <label>, Return, 59
GoTo <label>, 60
Gps.accuracy <nvar>, 129
Gps.altitude <nvar>, 129
Gps.bearing <nvar>, 129
Gps.close, 128
Gps.latitude <nvar>, 129
Gps.longitude <nvar>, 129
Gps.open, 128
Gps.provider <svar>, 128
Gps.speed <nvar>, 129
Gps.time <nvar>, 129
Gr.arc <object_number_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 110
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 115
Gr.bitmap.crop <new_bitmap_object_nvar>, <source_bitmap_object_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 116
Gr.bitmap.delete <bitmap_ptr_nvar>, 116
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nvar>, x , y, 117
Gr.bitmap.drawinto.end, 117
Gr.bitmap.drawinto.start <bitmap_ptr_nvar>, 117
gr.bitmap.load bitmap_ptr, File_name$, 115
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 116
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nvar>, width, height {, <smoothing_lvar> }, 116
Gr.bitmap.size <bitmap_ptr_nvar>, width, height, 116
Gr.bounded.touch touched, left, top, right, bottom, 113
Gr.bounded.touch2 touched, left, top, right, bottom, 113
Gr.brightness <nexp>, 109
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 119
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 120
Gr.camera.select 1|2, 118
Gr.camera.shoot <bm_ptr_nvar>, 119
Gr.circle <object_number_nvar>, x, y, radius, 111
Gr.clip <object__ptr_nvar>,  <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 122
Gr.close, 109
Gr.cls, 108
Gr.color alpha, red, green, blue, <style_nexp>, 107
Gr.front  flag, 109
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 117
Gr.get.params <object_ptr_nvar>, <param_array$[]>, 120
Gr.get.pixel x, y, alpha, red, green, blue, 120
Gr.get.position <object_ptr_nvar>,  x, y, 120
Gr.get.textbounds <sexp>, left, top, right, bottom, 114
Gr.get.type <object_ptr_nvar>, <type_svar>, 120
Gr.get.value <object_ptr_nvar>, <tag_sexp>, {<value_nvar | value_svar>}, 120
Gr.GetDL <dl_array[]> {, <keep_all_objects_lexp> }, 124
Gr.hide <object_number_nvar>, 112
Gr.line <object_number_nvar>, x1, y1, x2, y2, 110
Gr.modify <object_ptr_nvar>,  <tag_sexp>, {<value_nvar | value_svar>}, 121
Gr.NewDL <dl_array[{<start>,<length>}]>, 123
Gr.onGRTouch.resume, 113
Gr.open alpha, red, green, blue {, <ShowStatusBar_lexp> {, <orientation_nexp>}}, 106
Gr.orientation <nexp>, 107
Gr.oval <object_number_nvar>, left, top, right, bottom, 110
Gr.paint.get <object_ptr_nvar>, 122
Gr.point <object_number_nvar>, x, y, 109
Gr.poly <object_number_nvar>, list_pointer {,x,y}, 111
Gr.rect <object_number_nvar>, left, top, right, bottom, 110
Gr.render, 108
Gr.rotate.end {<obj_nvar>}, 118
Gr.rotate.start angle, x, y{,<obj_nvar>}, 117
Gr.save <filename_sexp> {,<quality_nexp>}, 120
Gr.scale x_factor, y_factor, 108
Gr.screen width, height{, density }, 108
Gr.screen.to_bitmap <bm_ptr_nvar>, 120
Gr.set.AntiAlias <lexp>, 107
Gr.set.pixels <object_number_nvar>, pixels[{<start>,<length>}] {,x,y}, 111
Gr.set.stroke <nexp>, 107
Gr.show <object_number_nvar>, 112
Gr.StatusBar.Show  <nexp>, 108
Gr.text.align type, 113
Gr.text.bold <lexp>, 114
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 115
Gr.text.size <nexp>, 113
Gr.text.skew <nexp>, 114
Gr.text.strike <lexp>, 114
Gr.text.typeface <nexp>, 114
Gr.text.underline <lexp>, 114
Gr.text.width <nvar>, <sexp>, 113
Gr.touch touched, x, y, 112
Gr.touch2 touched, x, y, 113
Gr_collision ( <object_1_nvar>, <object_2_nvar>), 50, 122
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 76
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 76
Headset <state_nvar>, <type_svar>, <mic_nvar>, 100
HEX$(<nexp>), 53
HEX(<sexp>), 49
Home, 101
Html.clear.cache, 81
Html.clear.history, 81
Html.close, 81
Html.get.datalink <data_svar>, 80
Html.go.back, 81
Html.go.forward, 81
Html.load.string <html_sexp>, 79
Html.load.url <file_sexp>, 79
Html.open {<show_status_bar_nexp>}, 79
Html.post <url_sexp>, <list_nexp>, 79
Http.post <url_sexp>, <list_nexp>, <result_svar>, 99
HYPOT(<nexp_x>, <nexp_y), 48
If  - Then - Else, 57
If  - Then - Else - Elseif - Endif, 56
Include FileNamePath, 94
Inkey$ <svar>, 70
Input <Prompt_sexp>, <nvar>|<svar>, {<Default_sexp>|<Default_nexp>}, 69
INT(<nexp>), 46
Is_In(<Search_for_sexp>, <Search_in_sexp>{, <start_nexp>}, 49
Kb.hide, 71
Kb.toggle, 70
Key.Resume, 63
LEFT$ (<sexp>, <nexp>), 51
LEN(<sexp>), 49
LET, 44
List.add <pointer_nexp>, <nexp>{,<nexp>...,<nexp>}, 36
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 37
List.add.array string_list_pointer, Array$[{<start>,<length>}], 37
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 37
List.clear <pointer_nexp>, 38
List.create N|S, <pointer_nvar>, 36
List.get <pointer_nexp>,<index_nexp>, <svar>|<nvar>, 37
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 37
List.remove <pointer_nexp>,<index_nexp>, 37
List.replace <pointer_nexp>,<index_nexp>, <sexp>|<nexp>, 37
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 38
List.size <pointer_nexp>, <nvar>, 38
List.ToArray <pointer_nexp>, Array$[] | Array[], 38
List.type <pointer_nexp>, <svar>, 38
LOG(<nexp>), 47
LOG10(<nexp>), 47
LOWER$(<sexp>), 53
MAX(<nexp>, <nexp>), 46
MenuKey.resume, 63
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 52
MIN(<nexp>, <nexp>), 46
MOD(<nexp1>, <nexp2>), 46
MyPhoneNumber <svar>, 99
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 100
OCT$(<nexp>, 53
OCT(<sexp>), 49
OnBackGround:, 101
OnBackKey:, 62
OnBtReadReady:, 88
OnConsoleTouch:, 62
OnError:, 61
OnGRTouch:, 113
OnKeyPress:, 63
OnMenuKey:, 63
OnTimer:, 93
Pause <ticks_nexp>, 94
Phone.call <sexp>, 99
Phone.rcv.init, 99
Phone.rcv.next  <state_nvar>, <number_svar>, 99
PI(), 48
Popup <message_sexp>, <x_nexp>, <y_nexp>, <duration_nexp>, 94
POW(<nexp1>, <nexp2>), 48
Print <sexp>|<nexp> {,|;} ... <sexp>|<nexp>{,|;}, 68
RANDOMIZE(<nexp>), 45
Read.data <number>|<string> {,<number>|<string>...,<number>|<string>}, 63
Read.from <nexp>, 64
Read.next  <svar>|<nvar>{,<svar>|<nvar>..., <svar>|<nvar>}, 64
REM, 42
REPLACE$( <target_sexp>, <argument_sexp>, <replace_sexp>), 52
RIGHT$(<sexp>, <nexp>), 52
RND(), 46
ROUND(<value_nexp>{, <count_nexp>{, <mode_sexp>}}), 46
Run <filename_sexp> {, <data_sexp>}, 60
Select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 94
Sensors.close, 131
Sensors.list <sensor_array$[]>, 130
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 130
Sensors.read sensor_type, p1, p2, p3, 130
SGN(<nexp>), 45
SHIFT (<value_nexp>, <bits_nexp>), 49
SIN(<nexp>), 48
SINH(<nexp>), 48
Sms.rcv.init, 99
Sms.rcv.next <svar>, 100
Sms.send <number_sexp>, <message_sexp>, 99
Socket.client.close, 83
Socket.client.connect <server_ip_sexp>, <port_nexp>, 82
Socket.client.read.file  <fw_nexp>, 82
Socket.client.read.line <line_svar>, 82
Socket.client.read.ready <nvar>, 82
Socket.client.server.ip <svar>, 82
Socket.client.status <status_nvar>, 82
Socket.client.write.bytes <sexp>, 83
Socket.client.write.file  <fr_nexp>, 83
Socket.client.write.line <line_sexp>, 83
Socket.myip  <svar>, 83
Socket.server.client.ip <nvar>, 85
Socket.server.close, 85
Socket.server.connect {<wait_lexp>}, 83
Socket.server.create <port_nexp>, 83
Socket.server.disconnect, 85
Socket.server.read.file <fw_nexp>, 84
Socket.server.read.line <svar>, 84
Socket.server.read.ready <nvar>, 84
Socket.server.status <status_nvar>, 84
Socket.server.write.bytes <sexp>, 84
Socket.server.write.file  <fr_nexp>, 84
Socket.server.write.line <sexp>, 84
Soundpool.load <soundID_nvar>, <file_path_sexp>, 127
Soundpool.open <MaxStreams_nexp>, 127
Soundpool.pause <streamID_nexp>, 128
Soundpool.play <streamID_nvar>, <sounded_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 127
Soundpool.release, 128
Soundpool.resume <streamID_nexp>, 128
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 128
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 128
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 127
Soundpool.stop <streamID_nexp>, 128
Soundpool.unload <soundID_nexp>, 127
Split <result_array$[]>, <source_sexp> {, <test_sexp>}, 95
Split.all <result_array$[]>, <source_sexp> {, <test_sexp>}, 95
Sql.close <DB_pointer_nvar>, 102
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 104
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 102
Sql.exec <DB_pointer_nvar>, <command_sexp>, 104
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN$, 102
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 102
Sql.next <done_lvar>, <cursor_nvar>, C1V$, C2V$, ..., CNV$, 104
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 102
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp>, <order_sexp> } }, 103
Sql.query.length <length_nvar>, <cursor_nvar>, 103
Sql.query.position <position_nvar>, <cursor_nvar>, 103
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 104
Sql.update <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN$: <where_sexp>, 104
SQR(<nexp>), 45
Stack.clear <ptr_nexep>, 41
Stack.create N|S, <ptr_nvar>, 40
Stack.IsEmpty <ptr_nexep>, <nvar>, 41
Stack.peek <ptr_nexep>, <nvar>|<svar>, 41
Stack.pop <ptr_nexep>, <nvar>|<svar>, 41
Stack.push <ptr_nexep>, <nexp>|<sexp>, 41
Stack.type <ptr_nexep>, <svar>, 41
Starts_with (<Search_for_sexp>, <Search_in_sexp>{,<start_nexp>}, 50
STR$(<nexp>), 53
Stt.listen, 91
Stt.results <string_list_ptr_nvar>, 91
Su.close, 132
Su.open, 131
Su.read.line <svar>, 132
Su.read.ready <nvar>, 132
Su.write <sexp>, 132
Sw.begin <nexp>|<sexp>, 61
Sw.break, 61
Sw.case <nexp >|<sexp>, 61
Sw.default, 61
Sw.end, 61
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 90
System.close, 131
System.open, 131
System.read.line <svar>, 131
System.read.ready <nvar>, 131
System.write <sexp>, 131
TAN(<nexp>), 48
Text.close <file_table_nvar>, 75
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 70
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 74
Text.position.get  <file_table_nvar>, <position_nvar>, 75
Text.position.set <file_table_nvar>, <position_nexp>, 76
Text.readln <file_table_nvar>, <line_svar>, 75
Text.writeln <file_table_nexp>, <parms same as print>, 75
TGet <result_svar>, <prompt_sexp> {,  <title_sexp>}, 70
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 96
TIME(), 51
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 51
Timer.Clear, 93
Timer.Resume, 93
Timer.set <interval_nexp>, 93
TimeZone.get <tz_svar>, 97
TimeZone.list <tz_list_pointer_nvar>, 97
TimeZone.set { <tz_sexp> }, 97
TODEGREES(<nexp>), 49
Tone <frequency_nexp>, <duration_nexp>, 97
TORADIANS(<nexp>), 49
Tts.init, 90
Tts.speak  <sexp> {, <wait_lexp>}, 91
Tts.speak.toFile <sexp> {, <path_sexp>}, 91
Tts.stop, 91
UCODE(<sexp>), 49
UnDim Array[], Array$[], ..., 32
UPPER$(<sexp>), 53
VAL( <sexp> ), 49
VERSION$(), 53
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 97
W_R.break, 58
W_R.continue, 58
WakeLock <code_nexp>, 97, 98
While <lexp> - Repeat, 58
WORD$(<source_sexp>, <n_nexp> {, <test_sexp>}), 52
