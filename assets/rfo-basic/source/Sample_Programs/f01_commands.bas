 There is 150+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 45
!! - Block Comment, 46
# - Format Line, 25
% - Middle of Line Comment, 46
ABS(<nexp>), 49
ACOS(<nexp>), 52
Array. reverse Array$[{<start>,<length>}], 37
Array. reverse Array[{<start>,<length>}], 37
Array.average <Average_nvar>, Array[{<start>,<length>}], 36
Array.copy SourceArray$[{<start>,<length>}], DestinationArray$[{{-}<extras>}], 36
Array.copy SourceArray[{<start>,<length>}], DestinationArray[{{-}<extras>}], 36
Array.delete Array[], Array$[] ..., 37
Array.length n, Array$[{<start>,<length>}], 37
Array.length n, Array[{<start>,<length>}], 37
Array.load Array$[], <sexp>, ..., 37
Array.load Array[], <nexp>, ..., 37
Array.max <max_nvar> Array[{<start>,<length>}], 37
Array.min <min_nvar>, Array[{<start>,<length>}], 37
Array.search Array$[{<start>,<length>}], <value_sexp>, <result_nvar>{,<start_nexp>}, 37
Array.search Array[{<start>,<length>}], <value_nexp>, <result_nvar>{,<start_nexp>}, 37
Array.shuffle Array[{<start>,<length>}], 38
Array.sort Array[{<start>,<length>}], 38
Array.std_dev <sd_nvar>, Array[{<start>,<length>}], 38
Array.sum <sum_nvar>, Array[{<start>,<length>}], 38
Array.variance <v_nvar>, Array[{<start>,<length>}], 38
ASCII(<sexp>{, <index_nexp>}), 53
ASIN(<nexp>), 52
ATAN(<nexp>), 52
ATAN2 (<nexp_y>, <nexp_x>), 52
Audio.isdone <lvar>, 147
Audio.length <length_nvar>, <aft_nexp>, 147
Audio.load <aft_nvar>, <filename_sexp>, 145
Audio.loop, 146
Audio.pause, 146
Audio.play <aft_nexp>, 146
Audio.position.current <nvar>, 147
Audio.position.seek <nexp>, 147
Audio.record.start <fn_svar>, 147
Audio.record.stop, 147
Audio.release <aft_nexp>, 147
Audio.stop, 146
Audio.volume <left_nexp>, <right_nexp>, 146
Back.resume, 73
BACKGROUND(), 54
Background.resume, 119
BAND(<nexp1>, <nexp2>), 49
BIN$(<nexp>), 57
BIN(<sexp>), 53
BOR(<nexp1>, <nexp2>), 49
Browse <url_sexp>, 97
Bt.close, 104
Bt.connect {0|1}, 104
Bt.device.name <svar>, 106
Bt.disconnect, 104
Bt.onReadReady.resume, 106
Bt.open {0|1}, 104
Bt.read.bytes <svar>, 106
Bt.read.ready <nvar>, 105
Bt.reconnect, 105
Bt.set.UUID <sexp>, 106
Bt.status <nvar>, 105
Bt.write {<exp> {,|;}} ..., 105
Bundle.clear <pointer_nexp>, 44
Bundle.contain <pointer_nexp>, <key_sexp> , <contains_nvar>, 43
Bundle.create <pointer_nvar>, 42
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 43
Bundle.keys <pointer_nexp>, <list_nvar>, 43
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 42
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 43
BXOR(<nexp1>, <nexp2>), 49
Byte.close <file_table_nexp>, 92
Byte.copy <file_table_nexp>,<output_file_sexp>, 94
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 92
Byte.position.get <file_table_nexp>, <position_nexp>, 93
Byte.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 93
Byte.position.set <file_table_nexp>, <position_nexp>, 93
Byte.read.buffer <file_table_nexp>, <count_nexp>, <buffer_svar>, 93
Byte.read.byte <file_table_nexp>, <byte_nvar>, 92
Byte.write.buffer <file_table_nexp>, <sexp>, 93
Byte.write.byte <file_table_nexp>, <byte_nexp>|<sexp>, 93
Call <user_defined_function>, 65
CBRT(<nexp>), 49
CEIL(<nexp>), 50
CHR$ (<nexp>, ...), 56
Clipboard.get <svar>, 106
Clipboard.put <sexp>, 106
CLOCK(), 55
Cls, 79
Console.line. count <count_nvar >, 80
Console.line.text <line_nexp>, <text_svar>, 80
Console.line.touched <line_nvar> {, <press_lvar>}, 80
Console.save <filename_sexp>, 80
Console.title { <title_sexp>}, 79
ConsoleTouch.resume, 73
COS(<nexp>), 52
COSH(<nexp>), 52
D_U.break, 68
D_U.continue, 68
Debug.dump.array Array[], 75
Debug.dump.bundle <bundlePtr_nexp>, 75
Debug.dump.list <listPtr_nexp>, 75
Debug.dump.scalars, 75
Debug.dump.stack <stackPtr_nexp>, 76
Debug.echo.off, 75
Debug.echo.on, 75
Debug.off, 75
Debug.on, 75
Debug.print, 75
Debug.show, 77
Debug.show.array Array[], 76
Debug.show.bundle <bundlePtr_nexp>, 76
Debug.show.list <listPtr_nexp>, 76
Debug.show.program, 77
Debug.show.scalars, 76
Debug.show.stack <stackPtr_nexp>, 77
Debug.show.watch, 77
Debug.watch var, ..., 77
Decrypt <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 107
Device <nvar>|<nexp>, 111
Device <svar>, 111
Dialog.message {<title_sexp>}, {<message_sexp>}, <selection_nvar> {, <button1_sexp>{, <button2_sexp>{, <button3_sexp>}}}, 81
Dialog.select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp>}, 82
Dim Array [n, n, ...], Array$[n, n, ...] ..., 36
Do / Until <lexp>, 68
Echo.off, 106
Echo.on, 106
Email.send <recipient_sexp>, <subject_sexp>, <body_sexp>, 118
Encrypt <pw_sexp>, <source_sexp>, <encrypted_svar>, 107
End{ <msg_sexp>}, 74
ENDS_WITH(<sub_sexp>, <base_sexp>), 54
Exit, 74
EXP(<nexp>), 51
F_N.break, 67
F_N.continue, 67
File.delete <lvar>, <path_sexp>, 87
File.dir <path_sexp>, Array[] {, <dirmark_sexp>}, 88
File.exists <lvar>, <path_sexp>, 88
File.mkdir <path_sexp>, 88
File.rename <old_path_sexp>, <new_path_sexp>, 88
File.root <svar>, 89
File.size <size_nvar>, <path_sexp>, 89
File.type <type_svar>, <path_sexp>, 89
FLOOR(<nexp>), 50
Fn.def name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 64
Fn.end, 65
Fn.rtn <sexp>|<nexp>, 65
Font.clear, 78
Font.delete {<font_ptr_nexp>}, 78
Font.load <font_ptr_nvar>, <filename_sexp>, 78
For - To - Step / Next, 66
FORMAT$(<pattern_sexp>, <nexp>), 62
FORMAT_USING$(<locale_sexp>, <format_sexp> { , <exp>}...), 62
FRAC(<nexp>), 50
Ftp.cd <new_directory_sexp>, 103
Ftp.close, 102
Ftp.delete <filename_sexp>, 103
Ftp.dir <list_nvar>, 102
Ftp.get <source_sexp>, <destination_sexp>, 102
Ftp.mkdir <directory_sexp>, 103
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 102
Ftp.put <source_sexp>, <destination_sexp>, 102
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 103
Ftp.rmdir <directory_sexp>, 103
GETERROR$(), 56
GoSub <index_nexp>, <label>... / Return, 70
GoSub <label> / Return, 69
GoTo <index_nexp>, <label>..., 69
GoTo <label>, 69
Gps.accuracy <nvar>, 150
Gps.altitude <nvar>, 150
Gps.bearing <nvar>, 150
Gps.close, 150
Gps.latitude <nvar>, 150
Gps.longitude <nvar>, 150
Gps.open, 150
Gps.provider <svar>, 150
Gps.satellites <nvar>, 150
Gps.speed <nvar>, 150
Gps.time <nvar>, 150
Gr.arc <object_number_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 129
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 136
Gr.bitmap.crop <new_bitmap_object_nvar>, <source_bitmap_object_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 137
Gr.bitmap.delete <bitmap_ptr_nvar>, 136
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nvar>, x , y, 137
Gr.bitmap.drawinto.end, 138
Gr.bitmap.drawinto.start <bitmap_ptr_nvar>, 137
gr.bitmap.load bitmap_ptr, File_name$, 136
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 137
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nvar>, width, height {, <smoothing_lexp> }, 136
Gr.bitmap.size <bitmap_ptr_nexp>, width, height, 136
Gr.bounded.touch touched, left, top, right, bottom, 131
Gr.bounded.touch2 touched, left, top, right, bottom, 132
Gr.brightness <nexp>, 128
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 140
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 140
Gr.camera.select 1|2, 139
Gr.camera.shoot <bm_ptr_nvar>, 139
Gr.circle <object_number_nvar>, x, y, radius, 129
Gr.clip <object__ptr_nvar>, <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 144
Gr.close, 127
Gr.cls, 127
Gr.color alpha, red, green, blue, <style_nexp>, 125
Gr.front flag, 127
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 137
Gr.get.params <object_ptr_nvar>, <param_array$[]>, 141
Gr.get.pixel x, y, alpha, red, green, blue, 140
Gr.get.position <object_ptr_nvar>, x, y, 141
Gr.get.textbounds <sexp>, left, top, right, bottom, 133
Gr.get.type <object_ptr_nvar>, <type_svar>, 141
Gr.get.value <object_ptr_nvar>, <tag_sexp>, {<value_nvar | value_svar>}, 141
Gr.getDL <dl_array[]> {, <keep_all_objects_lexp> }, 145
Gr.hide <object_number_nvar>, 130
Gr.line <object_number_nvar>, x1, y1, x2, y2, 128
Gr.modify <object_ptr_nvar>, {<tag_sexp>, <value_nvar | value_svar>}, ..., 141
Gr.newDL <dl_array[{<start>,<length>}]>, 145
Gr.onGrTouch.resume, 132
Gr.open alpha, red, green, blue {, <ShowStatusBar_lexp> {, <Orientation_nexp>}}, 125
Gr.orientation <nexp>, 126
Gr.oval <object_number_nvar>, left, top, right, bottom, 129
Gr.paint.get <object_ptr_nvar>, 143
Gr.point <object_number_nvar>, x, y, 128
Gr.poly <object_number_nvar>, list_pointer {,x,y}, 130
Gr.rect <object_number_nvar>, left, top, right, bottom, 128
Gr.render, 126
Gr.rotate.end {<obj_nvar>}, 138
Gr.rotate.start angle, x, y{,<obj_nvar>}, 138
Gr.save <filename_sexp> {,<quality_nexp>}, 140
Gr.scale x_factor, y_factor, 127
Gr.screen width, height{, density }, 126
Gr.screen.to_bitmap <bm_ptr_nvar>, 140
Gr.set.antialias <lexp>, 125
Gr.set.pixels <object_number_nvar>, pixels[{<start>,<length>}] {,x,y}, 129
Gr.set.stroke <nexp>, 126
Gr.show <object_number_nvar>, 130
Gr.statusbar {<height_nvar>} {, showing_lvar}, 126
Gr.statusbar.show <nexp>, 126
Gr.text.align type, 132
Gr.text.bold <lexp>, 135
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 135
Gr.text.height {<height_nvar>} {, <up_nvar>} {, <down_nvar>}, 132
Gr.text.setfont {<font_ptr_nexp>|<font_family_sexp>} {, <style_sexp>}, 133
Gr.text.size <nexp>, 132
Gr.text.skew <nexp>, 135
Gr.text.strike <lexp>, 135
Gr.text.typeface {<nexp>} {, <style_nexp>}, 134
Gr.text.underline <lexp>, 135
Gr.text.width <nvar>, <sexp>, 133
Gr.touch touched, x, y, 131
Gr.touch2 touched, x, y, 132
GR_COLLISION ( <object_1_nvar>, <object_2_nvar>), 54
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 91
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 91
Headset <state_nvar>, <type_svar>, <mic_nvar>, 118
HEX$(<nexp>), 57
HEX(<sexp>), 53
Home, 119
Html.clear.cache, 97
Html.clear.history, 97
Html.close, 97
Html.get.datalink <data_svar>, 95
Html.go.back, 96
Html.go.forward, 97
Html.load.string <html_sexp>, 95
Html.load.url <file_sexp>, 95
Html.open {<ShowStatusBar_lexp> {, <Orientation_nexp>}}, 94
Html.orientation <nexp>, 95
Html.post <url_sexp>, <list_nexp>, 95
Http.post <url_sexp>, <list_nexp>, <result_svar>, 97
HYPOT(<nexp_x>, <nexp_y), 52
If / Then / Else, 66
If / Then / Else / Elseif / Endif, 66
Include FileNamePath, 70
Inkey$ <svar>, 83
Input {<prompt_sexp>}, <result_var>{, <default_exp>},{<canceled_nvar>}, 82
INT$(<nexp>), 57
INT(<nexp>), 50
IS_IN(<sub_sexp>, <base_sexp>{, <start_nexp>}, 54
Kb.hide, 85
Kb.toggle, 85
Key.resume, 73
LEFT$ (<sexp>, <nexp>), 56
LEN(<sexp>), 53
Let, 48
List.add <pointer_nexp>, <nexp>{,<nexp>...,<nexp>}, 40
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 40
List.add.array string_list_pointer, Array$[{<start>,<length>}], 40
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 40
List.clear <pointer_nexp>, 41
List.create N|S, <pointer_nvar>, 40
List.get <pointer_nexp>,<index_nexp>, <svar>|<nvar>, 41
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 41
List.remove <pointer_nexp>,<index_nexp>, 41
List.replace <pointer_nexp>,<index_nexp>, <sexp>|<nexp>, 40
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 41
List.size <pointer_nexp>, <nvar>, 41
List.toArray <pointer_nexp>, Array$[] | Array[], 42
List.type <pointer_nexp>, <svar>, 41
LOG(<nexp>), 51
LOG10(<nexp>), 51
LOWER$(<sexp>), 57
MAX(<nexp>, <nexp>), 50
MenuKey.resume, 73
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 56
MIN(<nexp>, <nexp>), 50
MOD(<nexp1>, <nexp2>), 50
MyPhoneNumber <svar>, 117
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 118
OCT$(<nexp>, 57
OCT(<sexp>), 53
OnBackGround:, 119
OnBackKey:, 73
OnBtReadReady:, 105
OnConsoleTouch:, 72
OnError:, 72
OnGrTouch:, 132
OnKeyPress:, 73
OnMenuKey:, 73
OnTimer:, 110
Pause <ticks_nexp>, 113
Phone.call <sexp>, 117
Phone.info <nvar>|<nexp>, 112
Phone.rcv.init, 117
Phone.rcv.next <state_nvar>, <number_svar>, 117
PI(), 52
Popup <message_sexp>, <x_nexp>, <y_nexp>, <duration_nexp>, 83
POW(<nexp1>, <nexp2>), 52
Print {<exp> {,|;}} ..., 79
RANDOMIZE(<nexp>), 49
Read.data <number>|<string> {,<number>|<string>...,<number>|<string>}, 74
Read.from <nexp>, 74
Read.next <var>, ..., 74
Rem, 46
REPLACE$( <target_sexp>, <argument_sexp>, <replace_sexp>), 56
RIGHT$(<sexp>, <nexp>), 56
Ringer.get.mode <nvar>, 107
Ringer.get.volume <nvar>, 107
Ringer.get.volume <vol_nvar> { , <max_nvar>, 107
Ringer.set.mode <nexp>, 107
RND(), 50
ROUND(<value_nexp>{, <count_nexp>{, <mode_sexp>}}), 50
Run <filename_sexp> {, <data_sexp>}, 70
Select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 83
Sensors.close, 152
Sensors.list <sensor_array$[]>, 151
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 152
Sensors.read sensor_type, p1, p2, p3, 152
SGN(<nexp>), 49
SHIFT (<value_nexp>, <bits_nexp>), 53
SIN(<nexp>), 52
SINH(<nexp>), 52
Sms.rcv.init, 117
Sms.rcv.next <svar>, 118
Sms.send <number_sexp>, <message_sexp>, 117
Socket.client.close, 100
Socket.client.connect <server_sexp>, <port_nexp> { , <wait_lexp> }, 98
Socket.client.read.file <fw_nexp>, 99
Socket.client.read.line <line_svar>, 99
Socket.client.read.ready <nvar>, 99
Socket.client.server.ip <svar>, 99
Socket.client.status <status_nvar>, 98
Socket.client.write.bytes <sexp>, 99
Socket.client.write.file <fr_nexp>, 99
Socket.client.write.line <line_sexp>, 99
Socket.myIP <svar>, 100
Socket.server.client.ip <nvar>, 101
Socket.server.close, 101
Socket.server.connect {<wait_lexp>}, 100
Socket.server.create <port_nexp>, 100
Socket.server.disconnect, 101
Socket.server.read.file <fw_nexp>, 101
Socket.server.read.line <svar>, 100
Socket.server.read.ready <nvar>, 101
Socket.server.status <status_nvar>, 100
Socket.server.write.bytes <sexp>, 101
Socket.server.write.file <fr_nexp>, 101
Socket.server.write.line <sexp>, 101
Soundpool.load <soundID_nvar>, <file_path_sexp>, 148
Soundpool.open <MaxStreams_nexp>, 148
Soundpool.pause <streamID_nexp>, 149
Soundpool.play <streamID_nvar>, <sounded_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 148
Soundpool.release, 149
Soundpool.resume <streamID_nexp>, 149
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 149
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 149
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 149
Soundpool.stop <streamID_nexp>, 149
Soundpool.unload <soundID_nexp>, 148
Split <result_array$[]>, <source_sexp> {, <test_sexp>}, 113
Split.all <result_array$[]>, <source_sexp> {, <test_sexp>}, 113
Sql.close <DB_pointer_nvar>, 120
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 122
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 120
Sql.exec <DB_pointer_nvar>, <command_sexp>, 122
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$, ...,CN$, VN$, 120
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 120
Sql.next <done_lvar>, <cursor_nvar>, C1V$, C2V$, .., CNV$, 122
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 120
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp>, <order_sexp> } }, 121
Sql.query.length <length_nvar>, <cursor_nvar>, 121
Sql.query.position <position_nvar>, <cursor_nvar>, 121
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 122
Sql.update <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN$: <where_sexp>, 122
SQR(<nexp>), 49
Stack.clear <ptr_nexp>, 45
Stack.create N|S, <ptr_nvar>, 44
Stack.isEmpty <ptr_nexp>, <nvar>, 45
Stack.peek <ptr_nexp>, <nvar>|<svar>, 44
Stack.pop <ptr_nexp>, <nvar>|<svar>, 44
Stack.push <ptr_nexp>, <nexp>|<sexp>, 44
Stack.type <ptr_nexp>, <svar>, 45
STARTS_WITH (<sub_sexp>, <base_sexp>{, <start_nexp>}, 54
STR$(<nexp>), 57
Stt.listen, 109
Stt.results <string_list_ptr_nvar>, 109
Su.close, 153
Su.open, 153
Su.read.line <svar>, 153
Su.read.ready <nvar>, 153
Su.write <sexp>, 153
Sw.begin <nexp>|<sexp>, 71
Sw.break, 72
Sw.case <nexp >|<sexp>, 72
Sw.default, 72
Sw.end, 72
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 114
System.close, 153
System.open, 152
System.read.line <svar>, 153
System.read.ready <nvar>, 152
System.write <sexp>, 152
TAN(<nexp>), 52
Text.close <file_table_nexp>, 90
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 84
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 89
Text.position.get <file_table_nexp>, <position_nvar>, 91
Text.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 91
Text.position.set <file_table_nexp>, <position_nexp>, 91
Text.readln <file_table_nexp>, <line_svar>, 90
Text.writeln <file_table_nexp>, {<exp> {,|;}} ..., 90
TGet <result_svar>, <prompt_sexp> {, <title_sexp>}, 84
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 114
TIME(), 55
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 55
Timer.clear, 111
Timer.resume, 110
Timer.set <interval_nexp>, 110
TimeZone.get <tz_svar>, 115
TimeZone.list <tz_list_pointer_nvar>, 115
TimeZone.set { <tz_sexp> }, 115
TODEGREES(<nexp>), 53
Tone <frequency_nexp>, <duration_nexp>, 115
TORADIANS(<nexp>), 53
Tts.init, 108
Tts.speak <sexp> {, <wait_lexp>}, 108
Tts.speak.toFile <sexp> {, <path_sexp>}, 108
Tts.stop, 108
UCODE(<sexp>{, <index_nexp>}), 53
UnDim Array[], Array$[], ..., 36
UPPER$(<sexp>), 57
USING$({<locale_sexp>} , <format_sexp> { , <exp>}...), 58
VAL(<sexp>), 53
VERSION$(), 57
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 115
W_R.break, 68
W_R.continue, 67
WakeLock <code_nexp>, 116
While <lexp> / Repeat, 67
WifiLock <code_nexp>, 116
WORD$(<source_sexp>, <n_nexp> {, <test_sexp>}), 56
