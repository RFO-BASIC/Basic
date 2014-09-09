 There is 150+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 43
!! - Block Comment, 43
# - Format Line, 23
% - Middle of Line Comment, 44
ABS(<nexp>), 47
ACOS(<nexp>), 50
Array. reverse Array$[{<start>,<length>}], 35
Array. reverse Array[{<start>,<length>}], 35
Array.average <Average_nvar>, Array[{<start>,<length>}], 34
Array.copy SourceArray$[{<start>,<length>}], DestinationArray$[{{-}<extras>}], 34
Array.copy SourceArray[{<start>,<length>}], DestinationArray[{{-}<extras>}], 34
Array.delete Array[], Array$[] ..., 34
Array.length n, Array$[{<start>,<length>}], 34
Array.length n, Array[{<start>,<length>}], 34
Array.load Array$[], <sexp>, ..., 35
Array.load Array[], <nexp>, ..., 35
Array.max <max_nvar> Array[{<start>,<length>}], 35
Array.min <min_nvar>, Array[{<start>,<length>}], 35
Array.search Array$[{<start>,<length>}], <value_sexp>, <result_nvar>{,<start_nexp>}, 35
Array.search Array[{<start>,<length>}], <value_nexp>, <result_nvar>{,<start_nexp>}, 35
Array.shuffle Array[{<start>,<length>}], 35
Array.sort Array[{<start>,<length>}], 36
Array.std_dev <sd_nvar>, Array[{<start>,<length>}], 36
Array.sum <sum_nvar>, Array[{<start>,<length>}], 36
Array.variance <v_nvar>, Array[{<start>,<length>}], 36
ASCII(<sexp>{, <index_nexp>}), 51
ASIN(<nexp>), 50
ATAN(<nexp>), 50
ATAN2 (<nexp_x>, <nexp_y>), 50
Audio.isdone <lvar>, 136
Audio.length <length_nvar>, <aft_nexp>, 136
Audio.load  <aft_nvar>, <filename_sexp>, 135
Audio.loop, 135
Audio.pause, 135
Audio.play <aft_nexp>, 135
Audio.position.current  <nvar>, 136
Audio.position.seek <nexp>, 136
Audio.record.start <fn_svar>, 136
Audio.record.stop, 137
Audio.release <aft_nexp>, 136
Audio.stop, 135
Audio.volume <left_nexp>, <right_nexp>, 135
Back.resume, 70
Background(), 52
Background.Resume, 112
BAND(<nexp1>, <nexp2>), 46
BIN$(<nexp>), 55
BIN(<sexp>), 51
BOR(<nexp1>, <nexp2>), 46
Browse <url_sexp>, 98
Bt.close, 95
Bt.connect {0|1}, 96
Bt.device.name <svar>, 97
Bt.disconnect, 96
Bt.onReadReady.Resume, 97
Bt.open {0|1}, 95
Bt.read.bytes <svar>, 97
Bt.read.ready <nvar>, 97
Bt.reconnect, 96
Bt.set.uuid <sexp>, 97
Bt.status <nvar>, 96
Bt.write {<exp> {,|;}} ..., 96
Bundle.clear <pointer_nexp>, 41
Bundle.contain  <pointer_nexp>, <key_sexp> , <contains_nvar>, 41
Bundle.create <pointer_nvar>, 40
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 41
Bundle.keys <pointer_nexp>, <list_nvar>, 41
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 40
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 41
BXOR(<nexp1>, <nexp2>), 47
Byte.close <file_table_nvar>, 84
Byte.copy <file_table_nvar>,<output_file_svar>, 85
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 84
Byte.position.get  <file_table_nvar>, <position_nexp>, 85
Byte.position.set <file_table_nvar>, <position_nexp>, 85
Byte.read.buffer <file_table_nvar>, <count_nexp>, <buffer_svar>, 85
Byte.read.byte <file_table_nvar>, <byte_nvar>, 84
Byte.write.buffer <file_table_nvar>, <sexp>, 85
Byte.write.byte <file_table_nvar>, <byte_nexp>|<sexp>, 85
Call <user_defined_function>, 63
CBRT(<nexp>), 47
CEIL(<nexp>), 48
CHR$ (<nexp>, ...), 53
Clipboard.get <svar>, 98
Clipboard.put <sexp>, 98
Clock(), 53
CLS, 75
Console.line.text <line_nexp>, <text_svar>, 76
Console.line.touched <line_nvar> {, <press_lvar>}, 76
Console.save <filename_sexp>, 77
Console.Title { <title_sexp>}, 75
ConsoleTouch.Resume, 70
COS(<nexp>), 50
COSH(<nexp>), 50
D_U.break, 66
D_U.continue, 66
Debug.dump.array Array[], 73
Debug.dump.bundle  <bundlePtr_nexp>, 73
Debug.dump.list  <listPtr_nexp>, 73
Debug.dump.scalars, 72
Debug.dump.stack  <stackPtr_nexp>, 73
Debug.echo.off, 72
Debug.echo.on, 72
Debug.off, 72
Debug.on, 72
Debug.print, 72
Debug.show, 75
Debug.show.array Array[], 73
Debug.show.bundle <bundlePtr_nexp>, 73
Debug.show.list <listPtr_nexp>, 74
Debug.show.program, 74
Debug.show.scalars, 73
Debug.show.stack <stackPtr_nexp>, 74
Debug.show.watch, 74
Debug.watch var, ..., 74
Decrypt  <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 99
Device <nvar>, 103
Device <svar>, 103
Dim Array [n, n, ...], Array$[n, n, ...] ..., 33
Do / Until <lexp>, 65
Echo.off, 98
Echo.on, 98
Email.send <recipient_sexp>, <subject_sexp>, <body_sexp>, 111
Encrypt <pw_sexp>, <source_sexp>, <encrypted_svar>, 98
End{ <msg_sexp>}, 71
ENDS_WITH( <sub_sexp>, <base_sexp>), 52
Exit, 71
EXP(<nexp>), 49
F_N.break, 65
F_N.continue, 65
File.delete <lvar>, <path_sexp>, 80
File.dir <path_sexp>, Array[], 80
File.exists <lvar>, <path_sexp>, 80
File.mkdir <path_sexp>, 81
File.rename <Old_Path_sexp>, <New_Path_sexp>, 81
File.root <svar>, 81
File.size <size_nvar>, <path_sexp>, 81
File.type <type_nvar>, <path_sexp>, 81
FLOOR(<nexp>), 48
Fn.def  name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 61
Fn.end, 63
Fn.rtn <sexp>|<nexp>, 63
For - To - Step / Next, 64
FORMAT$(<pattern_sexp>, <nexp>), 60
FORMAT_USING$(<locale_sexp>, <format_sexp> { , <exp>}...), 59
FRAC(<nexp>), 48
Ftp.cd <new_directory_sexp>, 94
Ftp.close, 93
Ftp.delete <filename_sexp>, 94
Ftp.dir <list_nvar>, 94
Ftp.get <source_sexp>, <destination_sexp>, 93
Ftp.mkdir <directory_sexp>, 94
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 93
Ftp.put <source_sexp>, <destination_sexp>, 93
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 94
Ftp.rmdir <directory_sexp>, 94
GETERROR$(), 53
GoSub <index_nexp>, <label>... / Return, 67
GoSub <label> / Return, 67
GoTo <index_nexp>, <label>..., 67
GoTo <label>, 67
Gps.accuracy <nvar>, 139
Gps.altitude <nvar>, 139
Gps.bearing <nvar>, 139
Gps.close, 139
Gps.latitude <nvar>, 139
Gps.longitude <nvar>, 139
Gps.open, 139
Gps.provider <svar>, 139
Gps.speed <nvar>, 139
Gps.time <nvar>, 139
Gr.arc <object_number_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 121
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 126
Gr.bitmap.crop <new_bitmap_object_nvar>, <source_bitmap_object_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 127
Gr.bitmap.delete <bitmap_ptr_nvar>, 126
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nvar>, x , y, 127
Gr.bitmap.drawinto.end, 128
Gr.bitmap.drawinto.start <bitmap_ptr_nvar>, 127
gr.bitmap.load bitmap_ptr, File_name$, 126
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 127
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nvar>, width, height {, <smoothing_lvar> }, 126
Gr.bitmap.size <bitmap_ptr_nvar>, width, height, 126
Gr.bounded.touch touched, left, top, right, bottom, 123
Gr.bounded.touch2 touched, left, top, right, bottom, 124
Gr.brightness <nexp>, 120
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 130
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 130
Gr.camera.select 1|2, 129
Gr.camera.shoot <bm_ptr_nvar>, 129
Gr.circle <object_number_nvar>, x, y, radius, 121
Gr.clip <object__ptr_nvar>,  <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 133
Gr.close, 119
Gr.cls, 119
Gr.color alpha, red, green, blue, <style_nexp>, 117
Gr.front  flag, 119
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 127
Gr.get.params <object_ptr_nvar>, <param_array$[]>, 131
Gr.get.pixel x, y, alpha, red, green, blue, 130
Gr.get.position <object_ptr_nvar>,  x, y, 131
Gr.get.textbounds <sexp>, left, top, right, bottom, 124
Gr.get.type <object_ptr_nvar>, <type_svar>, 131
Gr.get.value <object_ptr_nvar>, <tag_sexp>, {<value_nvar | value_svar>}, 131
Gr.GetDL <dl_array[]> {, <keep_all_objects_lexp> }, 134
Gr.hide <object_number_nvar>, 122
Gr.line <object_number_nvar>, x1, y1, x2, y2, 120
Gr.modify <object_ptr_nvar>, {<tag_sexp>, <value_nvar | value_svar>}, ..., 131
Gr.NewDL <dl_array[{<start>,<length>}]>, 134
Gr.onGRTouch.resume, 124
Gr.open alpha, red, green, blue {, <ShowStatusBar_lexp> {, <Orientation_nexp>}}, 117
Gr.orientation <nexp>, 118
Gr.oval <object_number_nvar>, left, top, right, bottom, 121
Gr.paint.get <object_ptr_nvar>, 132
Gr.point <object_number_nvar>, x, y, 120
Gr.poly <object_number_nvar>, list_pointer {,x,y}, 122
Gr.rect <object_number_nvar>, left, top, right, bottom, 120
Gr.render, 118
Gr.rotate.end {<obj_nvar>}, 128
Gr.rotate.start angle, x, y{,<obj_nvar>}, 128
Gr.save <filename_sexp> {,<quality_nexp>}, 130
Gr.scale x_factor, y_factor, 119
Gr.screen width, height{, density }, 118
Gr.screen.to_bitmap <bm_ptr_nvar>, 130
Gr.set.AntiAlias <lexp>, 118
Gr.set.pixels <object_number_nvar>, pixels[{<start>,<length>}] {,x,y}, 121
Gr.set.stroke <nexp>, 118
Gr.show <object_number_nvar>, 122
Gr.StatusBar.Show  <nexp>, 118
Gr.text.align type, 124
Gr.text.bold <lexp>, 125
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 125
Gr.text.size <nexp>, 124
Gr.text.skew <nexp>, 125
Gr.text.strike <lexp>, 125
Gr.text.typeface <nexp>, 125
Gr.text.underline <lexp>, 125
Gr.text.width <nvar>, <sexp>, 124
Gr.touch touched, x, y, 123
Gr.touch2 touched, x, y, 124
Gr_collision ( <object_1_nvar>, <object_2_nvar>), 133
GR_COLLISION(<object_1_nvar>, <object_2_nvar>), 52
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 84
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 83
Headset <state_nvar>, <type_svar>, <mic_nvar>, 111
HEX$(<nexp>), 55
HEX(<sexp>), 51
Home, 112
Html.clear.cache, 89
Html.clear.history, 89
Html.close, 89
Html.get.datalink <data_svar>, 87
Html.go.back, 88
Html.go.forward, 88
Html.load.string <html_sexp>, 87
Html.load.url <file_sexp>, 87
Html.open {<ShowStatusBar_lexp> {, <Orientation_nexp>}}, 86
Html.orientation <nexp>, 86
Html.post <url_sexp>, <list_nexp>, 87
Http.post <url_sexp>, <list_nexp>, <result_svar>, 109
HYPOT(<nexp_x>, <nexp_y), 49
If / Then / Else, 64
If / Then / Else / Elseif / Endif, 63
Include FileNamePath, 104
Inkey$ <svar>, 77
Input {<prompt_sexp>}, <var>{, <default_exp>}, 77
INT$(<nexp>), 55
INT(<nexp>), 48
IS_IN(<sub_sexp>, < base_sexp>{, <start_nexp>}, 51
Kb.hide, 78
Kb.toggle, 78
Key.Resume, 71
LEFT$ (<sexp>, <nexp>), 53
LEN(<sexp>), 50
LET, 45
List.add <pointer_nexp>, <nexp>{,<nexp>...,<nexp>}, 38
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 38
List.add.array string_list_pointer, Array$[{<start>,<length>}], 38
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 38
List.clear <pointer_nexp>, 39
List.create N|S, <pointer_nvar>, 38
List.get <pointer_nexp>,<index_nexp>, <svar>|<nvar>, 39
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 39
List.remove <pointer_nexp>,<index_nexp>, 39
List.replace <pointer_nexp>,<index_nexp>, <sexp>|<nexp>, 38
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 39
List.size <pointer_nexp>, <nvar>, 39
List.ToArray <pointer_nexp>, Array$[] | Array[], 40
List.type <pointer_nexp>, <svar>, 39
LOG(<nexp>), 49
LOG10(<nexp>), 49
LOWER$(<sexp>), 55
MAX(<nexp>, <nexp>), 48
MenuKey.resume, 70
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 54
MIN(<nexp>, <nexp>), 48
MOD(<nexp1>, <nexp2>), 48
MyPhoneNumber <svar>, 110
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 111
OCT$(<nexp>, 55
OCT(<sexp>), 51
OnBackGround:, 112
OnBackKey:, 70
OnBtReadReady:, 97
OnConsoleTouch:, 70
OnError:, 69
OnGRTouch:, 124
OnKeyPress:, 71
OnMenuKey:, 70
OnTimer:, 102
Pause <ticks_nexp>, 105
Phone.call <sexp>, 110
Phone.info <nvar>, 104
Phone.rcv.init, 110
Phone.rcv.next  <state_nvar>, <number_svar>, 110
PI(), 49
Popup <message_sexp>, <x_nexp>, <y_nexp>, <duration_nexp>, 105
POW(<nexp1>, <nexp2>), 49
Print {<exp> {,|;}} ..., 75
RANDOMIZE(<nexp>), 47
Read.data <number>|<string> {,<number>|<string>...,<number>|<string>}, 71
Read.from <nexp>, 72
Read.next <var>, ..., 71
REM, 43
REPLACE$( <target_sexp>, <argument_sexp>, <replace_sexp>), 54
RIGHT$(<sexp>, <nexp>), 54
Ringer.get.mode <nvar>, 99
Ringer.get.volume <nvar>, 99
Ringer.get.volume <vol_nvar> { , <max_nvar>, 99
Ringer.set.mode <nexp>, 99
RND(), 47
ROUND(<value_nexp>{, <count_nexp>{, <mode_sexp>}}), 48
Run <filename_sexp> {, <data_sexp>}, 68
Select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 105
Sensors.close, 141
Sensors.list <sensor_array$[]>, 140
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 141
Sensors.read sensor_type, p1, p2, p3, 141
SGN(<nexp>), 47
SHIFT (<value_nexp>, <bits_nexp>), 51
SIN(<nexp>), 49
SINH(<nexp>), 50
Sms.rcv.init, 110
Sms.rcv.next <svar>, 110
Sms.send <number_sexp>, <message_sexp>, 110
Socket.client.close, 91
Socket.client.connect <server_sexp>, <port_nexp> { , <wait_lexp> }, 90
Socket.client.read.file  <fw_nexp>, 90
Socket.client.read.line <line_svar>, 90
Socket.client.read.ready <nvar>, 90
Socket.client.server.ip <svar>, 90
Socket.client.status <status_nvar>, 90
Socket.client.write.bytes <sexp>, 91
Socket.client.write.file <fr_nexp>, 91
Socket.client.write.line <line_sexp>, 91
Socket.myip <svar>, 91
Socket.server.client.ip <nvar>, 93
Socket.server.close, 93
Socket.server.connect {<wait_lexp>}, 91
Socket.server.create <port_nexp>, 91
Socket.server.disconnect, 93
Socket.server.read.file <fw_nexp>, 93
Socket.server.read.line <svar>, 92
Socket.server.read.ready <nvar>, 92
Socket.server.status <status_nvar>, 92
Socket.server.write.bytes <sexp>, 92
Socket.server.write.file  <fr_nexp>, 92
Socket.server.write.line <sexp>, 92
Soundpool.load <soundID_nvar>, <file_path_sexp>, 137
Soundpool.open <MaxStreams_nexp>, 137
Soundpool.pause <streamID_nexp>, 138
Soundpool.play <streamID_nvar>, <sounded_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 138
Soundpool.release, 138
Soundpool.resume <streamID_nexp>, 138
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 138
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 138
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 138
Soundpool.stop <streamID_nexp>, 138
Soundpool.unload <soundID_nexp>, 137
Split <result_array$[]>, <source_sexp> {, <test_sexp>}, 106
Split.all <result_array$[]>, <source_sexp> {, <test_sexp>}, 106
Sql.close <DB_pointer_nvar>, 113
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 115
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 113
Sql.exec <DB_pointer_nvar>, <command_sexp>, 115
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$, ...,CN$, VN$, 113
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 113
Sql.next <done_lvar>, <cursor_nvar>, C1V$, C2V$, .., CNV$, 114
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 112
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp>, <order_sexp> } }, 114
Sql.query.length <length_nvar>, <cursor_nvar>, 114
Sql.query.position <position_nvar>, <cursor_nvar>, 114
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 115
Sql.update <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN$: <where_sexp>, 115
SQR(<nexp>), 47
Stack.clear <ptr_nexp>, 43
Stack.create N|S, <ptr_nvar>, 42
Stack.IsEmpty <ptr_nexp>, <nvar>, 43
Stack.peek <ptr_nexp>, <nvar>|<svar>, 42
Stack.pop <ptr_nexp>, <nvar>|<svar>, 42
Stack.push <ptr_nexp>, <nexp>|<sexp>, 42
Stack.type <ptr_nexp>, <svar>, 42
STARTS_WITH (<sub_sexp>, <base_sexp>{,<start_nexp>}, 52
STR$(<nexp>), 55
Stt.listen, 100
Stt.results <string_list_ptr_nvar>, 100
Su.close, 142
Su.open, 142
Su.read.line <svar>, 142
Su.read.ready <nvar>, 142
Su.write <sexp>, 142
Sw.begin <nexp>|<sexp>, 69
Sw.break, 69
Sw.case <nexp >|<sexp>, 69
Sw.default, 69
Sw.end, 69
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 98
System.close, 142
System.open, 141
System.read.line <svar>, 142
System.read.ready <nvar>, 141
System.write <sexp>, 141
TAN(<nexp>), 50
Text.close <file_table_nvar>, 82
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 77
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 82
Text.position.get  <file_table_nvar>, <position_nvar>, 83
Text.position.set <file_table_nvar>, <position_nexp>, 83
Text.readln <file_table_nvar>, <line_svar>, 82
Text.writeln <file_table_nexp>, {<exp> {,|;}} ..., 83
TGet <result_svar>, <prompt_sexp> {, <title_sexp>}, 78
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 106
TIME(), 53
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 53
Timer.Clear, 102
Timer.Resume, 102
Timer.set <interval_nexp>, 102
TimeZone.get <tz_svar>, 107
TimeZone.list <tz_list_pointer_nvar>, 108
TimeZone.set { <tz_sexp> }, 107
TODEGREES(<nexp>), 50
Tone <frequency_nexp>, <duration_nexp>, 108
TORADIANS(<nexp>), 50
Tts.init, 100
Tts.speak  <sexp> {, <wait_lexp>}, 100
Tts.speak.toFile <sexp> {, <path_sexp>}, 100
Tts.stop, 100
UCODE(<sexp>{, <index_nexp>}), 51
UnDim Array[], Array$[], ..., 34
UPPER$(<sexp>), 55
USING$({<locale_sexp>} , <format_sexp> { , <exp>}...), 55
VAL( <sexp> ), 50
VERSION$(), 55
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 108
W_R.break, 65
W_R.continue, 65
WakeLock <code_nexp>, 108
While <lexp> / Repeat, 65
WifiLock <code_nexp>, 109
WORD$(<source_sexp>, <n_nexp> {, <test_sexp>}), 54
