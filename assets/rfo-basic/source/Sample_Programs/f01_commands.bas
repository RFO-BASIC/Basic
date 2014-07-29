 There is 150+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 43
!! - Block Comment, 43
# - Format Line, 23
% - Middle of Line Comment, 43
ABS(<nexp>), 46
ACOS(<nexp>), 49
Array. reverse Array$[{<start>,<length>}], 35
Array. reverse Array[{<start>,<length>}], 35
Array.average <Average_nvar>, Array[{<start>,<length>}], 33
Array.copy SourceArray$[{<start>,<length>}], DestinationArray$[{{-}<extras>}], 33
Array.copy SourceArray[{<start>,<length>}], DestinationArray[{{-}<extras>}], 33
Array.delete Array[], Array$[] ..., 34
Array.length n, Array$[{<start>,<length>}], 34
Array.length n, Array[{<start>,<length>}], 34
Array.load Array$[], <sexp>, ..., 34
Array.load Array[], <nexp>, ..., 34
Array.max <max_nvar> Array[{<start>,<length>}], 34
Array.min <min_nvar>, Array[{<start>,<length>}], 34
Array.search Array$[{<start>,<length>}], <value_sexp>, <result_nvar>{,<start_nexp>}, 35
Array.search Array[{<start>,<length>}], <value_nexp>, <result_nvar>{,<start_nexp>}, 35
Array.shuffle Array[{<start>,<length>}], 35
Array.sort Array[{<start>,<length>}], 35
Array.std_dev <sd_nvar>, Array[{<start>,<length>}], 35
Array.sum <sum_nvar>, Array[{<start>,<length>}], 35
Array.variance <v_nvar>, Array[{<start>,<length>}], 35
ASCII(<sexp>), 50
ASIN(<nexp>), 49
ATAN(<nexp>), 49
ATAN2 (<nexp_x>, <nexp_y>), 49
Audio.isdone <lvar>, 133
Audio.length <length_nvar>, <aft_nexp>, 133
Audio.load  <aft_nvar>, <filename_sexp>, 132
Audio.loop, 132
Audio.pause, 132
Audio.play <aft_nexp>, 132
Audio.position.current  <nvar>, 133
Audio.position.seek <nexp>, 133
Audio.record.start <fn_svar>, 133
Audio.record.stop, 134
Audio.release <aft_nexp>, 133
Audio.stop, 132
Audio.volume <left_nexp>, <right_nexp>, 132
Back.resume, 69
Background(), 51
Background.Resume, 109
BAND(<nexp1>, <nexp2>), 46
BIN$(<nexp>), 54
BIN(<sexp>), 50
BOR(<nexp1>, <nexp2>), 46
Browse <url_sexp>, 96
Bt.close, 94
Bt.connect {0|1}, 94
Bt.device.name <svar>, 96
Bt.disconnect, 95
Bt.onReadReady.Resume, 96
Bt.open {0|1}, 94
Bt.read.bytes <svar>, 96
Bt.read.ready <nvar>, 95
Bt.reconnect, 95
Bt.set.uuid <sexp>, 96
Bt.status <nvar>, 95
Bt.write {<exp> {,|;}} ..., 95
Bundle.clear <pointer_nexp>, 41
Bundle.contain  <pointer_nexp>, <key_sexp> , <contains_nvar>, 40
Bundle.create <pointer_nvar>, 39
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 40
Bundle.keys <pointer_nexp>, <list_nvar>, 40
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 40
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 41
BXOR(<nexp1>, <nexp2>), 46
Byte.close <file_table_nvar>, 83
Byte.copy <file_table_nvar>,<output_file_svar>, 84
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 83
Byte.position.get  <file_table_nvar>, <position_nexp>, 84
Byte.position.set <file_table_nvar>, <position_nexp>, 84
Byte.read.buffer <file_table_nvar>, <count_nexp>, <buffer_svar>, 84
Byte.read.byte <file_table_nvar>, <byte_nvar>, 83
Byte.write.buffer <file_table_nvar>, <sexp>, 84
Byte.write.byte <file_table_nvar>, <byte_nexp>|<sexp>, 84
Call <user_defined_function>, 62
CBRT(<nexp>), 46
CEIL(<nexp>), 47
CHR$ (<nexp>), 52
Clipboard.get <svar>, 97
Clipboard.put <sexp>, 97
Clock(), 52
CLS, 74
Console.line.text <line_nexp>, <text_svar>, 75
Console.line.touched <line_nvar> {, <press_lvar>}, 75
Console.save <filename_sexp>, 75
Console.Title { <title_sexp>}, 74
ConsoleTouch.Resume, 69
COS(<nexp>), 49
COSH(<nexp>), 49
D_U.break, 64
D_U.continue, 64
Debug.dump.array Array[], 71
Debug.dump.bundle  <bundlePtr_nexp>, 71
Debug.dump.list  <listPtr_nexp>, 71
Debug.dump.scalars, 71
Debug.dump.stack  <stackPtr_nexp>, 71
Debug.echo.off, 71
Debug.echo.on, 71
Debug.off, 71
Debug.on, 71
Debug.print, 71
Debug.show, 73
Debug.show.array Array[], 72
Debug.show.bundle <bundlePtr_nexp>, 72
Debug.show.list <listPtr_nexp>, 72
Debug.show.program, 73
Debug.show.scalars, 71
Debug.show.stack <stackPtr_nexp>, 73
Debug.show.watch, 73
Debug.watch var, ..., 73
Decrypt  <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 97
Device <nvar>, 101
Device <svar>, 101
Dim Array [n, n, ...], Array$[n, n, ...] ..., 33
Do / Until <lexp>, 64
Echo.off, 97
Echo.on, 97
Email.send <recipient_sxep>, <subject_sexp>, <body_sexp>, 108
Encrypt <pw_sexp>, <source_sexp>, <encrypted_svar>, 97
End{ <msg_sexp>}, 69
Ends_with ( <Look_for_sexp>, <look_in_sexp>), 51
Exit, 70
EXP(<nexp>), 48
F_N.break, 63
F_N.continue, 63
File.delete <lvar>, <path_sexp>, 79
File.dir <path_sexp>, Array[], 79
File.exists <lvar>, <path_sexp>, 79
File.mkdir <path_sexp>, 79
File.rename <Old_Path_sexp>, <New_Path_sexp>, 79
File.root <svar>, 80
File.size <size_nvar>, <path_sexp>, 80
File.type <type_nvar>, <path_sexp>, 80
FLOOR(<nexp>), 47
Fn.def  name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 60
Fn.end, 62
Fn.rtn <sexp>|<nexp>, 62
For - To - Step / Next, 63
FORMAT$(<pattern_sexp>, <nexp>), 59
FORMAT_USING$(<locale_sexp>, <format_sexp> { , <exp>}...), 59
FRAC(<nexp>), 47
Ftp.cd <new_directory_sexp>, 93
Ftp.close, 92
Ftp.delete <filename_sexp>, 93
Ftp.dir <list_nvar>, 93
Ftp.get <source_sexp>, <destination_sexp>, 92
Ftp.mkdir <directory_sexp>, 93
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 92
Ftp.put <source_sexp>, <destination_sexp>, 92
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 93
Ftp.rmdir <directory_sexp>, 93
GETERROR$(), 52
GoSub <index_nexp>, <label>... / Return, 66
GoSub <label> / Return, 65
GoTo <index_nexp>, <label>..., 65
GoTo <label>, 65
Gps.accuracy <nvar>, 136
Gps.altitude <nvar>, 136
Gps.bearing <nvar>, 136
Gps.close, 136
Gps.latitude <nvar>, 136
Gps.longitude <nvar>, 136
Gps.open, 136
Gps.provider <svar>, 136
Gps.speed <nvar>, 136
Gps.time <nvar>, 136
Gr.arc <object_number_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 118
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 123
Gr.bitmap.crop <new_bitmap_object_nvar>, <source_bitmap_object_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 124
Gr.bitmap.delete <bitmap_ptr_nvar>, 123
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nvar>, x , y, 124
Gr.bitmap.drawinto.end, 125
Gr.bitmap.drawinto.start <bitmap_ptr_nvar>, 124
gr.bitmap.load bitmap_ptr, File_name$, 123
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 124
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nvar>, width, height {, <smoothing_lvar> }, 123
Gr.bitmap.size <bitmap_ptr_nvar>, width, height, 123
Gr.bounded.touch touched, left, top, right, bottom, 120
Gr.bounded.touch2 touched, left, top, right, bottom, 121
Gr.brightness <nexp>, 117
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 127
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 127
Gr.camera.select 1|2, 126
Gr.camera.shoot <bm_ptr_nvar>, 126
Gr.circle <object_number_nvar>, x, y, radius, 118
Gr.clip <object__ptr_nvar>,  <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 130
Gr.close, 116
Gr.cls, 116
Gr.color alpha, red, green, blue, <style_nexp>, 114
Gr.front  flag, 116
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 124
Gr.get.params <object_ptr_nvar>, <param_array$[]>, 128
Gr.get.pixel x, y, alpha, red, green, blue, 127
Gr.get.position <object_ptr_nvar>,  x, y, 128
Gr.get.textbounds <sexp>, left, top, right, bottom, 121
Gr.get.type <object_ptr_nvar>, <type_svar>, 128
Gr.get.value <object_ptr_nvar>, <tag_sexp>, {<value_nvar | value_svar>}, 128
Gr.GetDL <dl_array[]> {, <keep_all_objects_lexp> }, 131
Gr.hide <object_number_nvar>, 119
Gr.line <object_number_nvar>, x1, y1, x2, y2, 117
Gr.modify <object_ptr_nvar>,  <tag_sexp>, {<value_nvar | value_svar>}, 128
Gr.NewDL <dl_array[{<start>,<length>}]>, 131
Gr.onGRTouch.resume, 121
Gr.open alpha, red, green, blue {, <ShowStatusBar_lexp> {, <Orientation_nexp>}}, 114
Gr.orientation <nexp>, 115
Gr.oval <object_number_nvar>, left, top, right, bottom, 118
Gr.paint.get <object_ptr_nvar>, 129
Gr.point <object_number_nvar>, x, y, 117
Gr.poly <object_number_nvar>, list_pointer {,x,y}, 119
Gr.rect <object_number_nvar>, left, top, right, bottom, 117
Gr.render, 115
Gr.rotate.end {<obj_nvar>}, 125
Gr.rotate.start angle, x, y{,<obj_nvar>}, 125
Gr.save <filename_sexp> {,<quality_nexp>}, 127
Gr.scale x_factor, y_factor, 116
Gr.screen width, height{, density }, 115
Gr.screen.to_bitmap <bm_ptr_nvar>, 127
Gr.set.AntiAlias <lexp>, 115
Gr.set.pixels <object_number_nvar>, pixels[{<start>,<length>}] {,x,y}, 118
Gr.set.stroke <nexp>, 115
Gr.show <object_number_nvar>, 119
Gr.StatusBar.Show  <nexp>, 115
Gr.text.align type, 121
Gr.text.bold <lexp>, 122
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 122
Gr.text.size <nexp>, 121
Gr.text.skew <nexp>, 122
Gr.text.strike <lexp>, 122
Gr.text.typeface <nexp>, 122
Gr.text.underline <lexp>, 122
Gr.text.width <nvar>, <sexp>, 121
Gr.touch touched, x, y, 120
Gr.touch2 touched, x, y, 121
Gr_collision ( <object_1_nvar>, <object_2_nvar>), 51, 130
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 82
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 82
Headset <state_nvar>, <type_svar>, <mic_nvar>, 108
HEX$(<nexp>), 54
HEX(<sexp>), 50
Home, 109
Html.clear.cache, 87
Html.clear.history, 88
Html.close, 87
Html.get.datalink <data_svar>, 86
Html.go.back, 87
Html.go.forward, 87
Html.load.string <html_sexp>, 86
Html.load.url <file_sexp>, 85
Html.open {<ShowStatusBar_lexp> {, <Orientation_nexp>}}, 85
Html.orientation <nexp>, 85
Html.post <url_sexp>, <list_nexp>, 86
Http.post <url_sexp>, <list_nexp>, <result_svar>, 106
HYPOT(<nexp_x>, <nexp_y), 49
If / Then / Else, 62
If / Then / Else / Elseif / Endif, 62
Include FileNamePath, 101
Inkey$ <svar>, 76
Input <Prompt_sexp>, <nvar>|<svar>, {<Default_sexp>|<Default_nexp>}, 75
INT$(<nexp>), 54
INT(<nexp>), 47
Is_In(<Search_for_sexp>, <Search_in_sexp>{, <start_nexp>}, 50
Kb.hide, 77
Kb.toggle, 77
Key.Resume, 69
LEFT$ (<sexp>, <nexp>), 53
LEN(<sexp>), 50
LET, 45
List.add <pointer_nexp>, <nexp>{,<nexp>...,<nexp>}, 37
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 38
List.add.array string_list_pointer, Array$[{<start>,<length>}], 38
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 38
List.clear <pointer_nexp>, 39
List.create N|S, <pointer_nvar>, 37
List.get <pointer_nexp>,<index_nexp>, <svar>|<nvar>, 38
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 38
List.remove <pointer_nexp>,<index_nexp>, 38
List.replace <pointer_nexp>,<index_nexp>, <sexp>|<nexp>, 38
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 39
List.size <pointer_nexp>, <nvar>, 39
List.ToArray <pointer_nexp>, Array$[] | Array[], 39
List.type <pointer_nexp>, <svar>, 38
LOG(<nexp>), 48
LOG10(<nexp>), 48
LOWER$(<sexp>), 54
MAX(<nexp>, <nexp>), 47
MenuKey.resume, 69
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 53
MIN(<nexp>, <nexp>), 47
MOD(<nexp1>, <nexp2>), 47
MyPhoneNumber <svar>, 107
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 108
OCT$(<nexp>, 54
OCT(<sexp>), 50
OnBackGround:, 109
OnBackKey:, 69
OnBtReadReady:, 95
OnConsoleTouch:, 68
OnError:, 68
OnGRTouch:, 121
OnKeyPress:, 69
OnMenuKey:, 69
OnTimer:, 101
Pause <ticks_nexp>, 102
Phone.call <sexp>, 107
Phone.rcv.init, 107
Phone.rcv.next  <state_nvar>, <number_svar>, 107
PI(), 49
Popup <message_sexp>, <x_nexp>, <y_nexp>, <duration_nexp>, 102
POW(<nexp1>, <nexp2>), 49
Print {<exp> {,|;}} ..., 74
RANDOMIZE(<nexp>), 46
Read.data <number>|<string> {,<number>|<string>...,<number>|<string>}, 70
Read.from <nexp>, 70
Read.next <var>, ..., 70
REM, 43
REPLACE$( <target_sexp>, <argument_sexp>, <replace_sexp>), 53
RIGHT$(<sexp>, <nexp>), 53
Ringer.get.mode <nvar>, 98
Ringer.get.volume <nvar>, 98
Ringer.get.volume <vol_nvar> { , <max_nvar>, 98
Ringer.set.mode <nexp>, 98
RND(), 47
ROUND(<value_nexp>{, <count_nexp>{, <mode_sexp>}}), 47
Run <filename_sexp> {, <data_sexp>}, 66
Select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 102
Sensors.close, 138
Sensors.list <sensor_array$[]>, 137
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 138
Sensors.read sensor_type, p1, p2, p3, 138
SGN(<nexp>), 46
SHIFT (<value_nexp>, <bits_nexp>), 50
SIN(<nexp>), 49
SINH(<nexp>), 49
Sms.rcv.init, 107
Sms.rcv.next <svar>, 107
Sms.send <number_sexp>, <message_sexp>, 107
Socket.client.close, 90
Socket.client.connect <server_sexp>, <port_nexp> { , <wait_lexp> }, 88
Socket.client.read.file  <fw_nexp>, 89
Socket.client.read.line <line_svar>, 89
Socket.client.read.ready <nvar>, 89
Socket.client.server.ip <svar>, 89
Socket.client.status <status_nvar>, 89
Socket.client.write.bytes <sexp>, 90
Socket.client.write.file <fr_nexp>, 90
Socket.client.write.line <line_sexp>, 89
Socket.myip <svar>, 90
Socket.server.client.ip <nvar>, 92
Socket.server.close, 92
Socket.server.connect {<wait_lexp>}, 90
Socket.server.create <port_nexp>, 90
Socket.server.disconnect, 92
Socket.server.read.file <fw_nexp>, 91
Socket.server.read.line <svar>, 91
Socket.server.read.ready <nvar>, 91
Socket.server.status <status_nvar>, 90
Socket.server.write.bytes <sexp>, 91
Socket.server.write.file  <fr_nexp>, 91
Socket.server.write.line <sexp>, 91
Soundpool.load <soundID_nvar>, <file_path_sexp>, 134
Soundpool.open <MaxStreams_nexp>, 134
Soundpool.pause <streamID_nexp>, 135
Soundpool.play <streamID_nvar>, <sounded_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 135
Soundpool.release, 135
Soundpool.resume <streamID_nexp>, 135
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 135
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 135
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 135
Soundpool.stop <streamID_nexp>, 135
Soundpool.unload <soundID_nexp>, 134
Split <result_array$[]>, <source_sexp> {, <test_sexp>}, 103
Split.all <result_array$[]>, <source_sexp> {, <test_sexp>}, 103
Sql.close <DB_pointer_nvar>, 110
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 112
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 110
Sql.exec <DB_pointer_nvar>, <command_sexp>, 112
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$, ...,CN$, VN$, 110
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 110
Sql.next <done_lvar>, <cursor_nvar>, C1V$, C2V$, .., CNV$, 111
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 109
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp>, <order_sexp> } }, 111
Sql.query.length <length_nvar>, <cursor_nvar>, 111
Sql.query.position <position_nvar>, <cursor_nvar>, 111
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 112
Sql.update <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN$: <where_sexp>, 112
SQR(<nexp>), 46
Stack.clear <ptr_nexp>, 42
Stack.create N|S, <ptr_nvar>, 41
Stack.IsEmpty <ptr_nexp>, <nvar>, 42
Stack.peek <ptr_nexp>, <nvar>|<svar>, 42
Stack.pop <ptr_nexp>, <nvar>|<svar>, 41
Stack.push <ptr_nexp>, <nexp>|<sexp>, 41
Stack.type <ptr_nexp>, <svar>, 42
Starts_with (<Search_for_sexp>, <Search_in_sexp>{,<start_nexp>}, 51
STR$(<nexp>), 54
Stt.listen, 99
Stt.results <string_list_ptr_nvar>, 99
Su.close, 139
Su.open, 139
Su.read.line <svar>, 139
Su.read.ready <nvar>, 139
Su.write <sexp>, 139
Sw.begin <nexp>|<sexp>, 67
Sw.break, 68
Sw.case <nexp >|<sexp>, 67
Sw.default, 68
Sw.end, 68
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 97
System.close, 139
System.open, 138
System.read.line <svar>, 139
System.read.ready <nvar>, 138
System.write <sexp>, 138
TAN(<nexp>), 49
Text.close <file_table_nvar>, 81
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 76
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 80
Text.position.get  <file_table_nvar>, <position_nvar>, 82
Text.position.set <file_table_nvar>, <position_nexp>, 82
Text.readln <file_table_nvar>, <line_svar>, 81
Text.writeln <file_table_nexp>, {<exp> {,|;}} ..., 81
TGet <result_svar>, <prompt_sexp> {,  <title_sexp>}, 77
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 103
TIME(), 52
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 52
Timer.Clear, 101
Timer.Resume, 101
Timer.set <interval_nexp>, 101
TimeZone.get <tz_svar>, 104
TimeZone.list <tz_list_pointer_nvar>, 105
TimeZone.set { <tz_sexp> }, 104
TODEGREES(<nexp>), 50
Tone <frequency_nexp>, <duration_nexp>, 105
TORADIANS(<nexp>), 50
Tts.init, 98
Tts.speak  <sexp> {, <wait_lexp>}, 98
Tts.speak.toFile <sexp> {, <path_sexp>}, 98
Tts.stop, 99
UCODE(<sexp>), 50
UnDim Array[], Array$[], ..., 33
UPPER$(<sexp>), 54
USING$({<locale_sexp>} , <format_sexp> { , <exp>}...), 54
VAL( <sexp> ), 50
VERSION$(), 54
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 105
W_R.break, 64
W_R.continue, 64
WakeLock <code_nexp>, 105
While <lexp> / Repeat, 63
WifiLock <code_nexp>, 106
WORD$(<source_sexp>, <n_nexp> {, <test_sexp>}), 53
