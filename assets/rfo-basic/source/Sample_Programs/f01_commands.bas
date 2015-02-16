 There is 150+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 47
!! - Block Comment, 47
# - Format Line, 25
% - Middle of Line Comment, 47
ABS(<nexp>), 51
ACOS(<nexp>), 54
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
ASCII(<sexp>{, <index_nexp>}), 55
ASIN(<nexp>), 54
ATAN(<nexp>), 54
ATAN2 (<nexp_y>, <nexp_x>), 54
Audio.isdone <lvar>, 150
Audio.length <length_nvar>, <aft_nexp>, 150
Audio.load <aft_nvar>, <filename_sexp>, 149
Audio.loop, 149
Audio.pause, 149
Audio.play <aft_nexp>, 149
Audio.position.current <nvar>, 150
Audio.position.seek <nexp>, 150
Audio.record.start <fn_svar>, 150
Audio.record.stop, 151
Audio.release <aft_nexp>, 150
Audio.stop, 149
Audio.volume <left_nexp>, <right_nexp>, 149
Back.resume, 75
BACKGROUND(), 56
Background.resume, 122
BAND(<nexp1>, <nexp2>), 50
BIN$(<nexp>), 59
BIN(<sexp>), 55
BNOT(<nexp>, 51
BOR(<nexp1>, <nexp2>), 50
Browse <url_sexp>, 99
Bt.close, 107
Bt.connect {0|1}, 107
Bt.device.name <svar>, 108
Bt.disconnect, 107
Bt.onReadReady.resume, 108
Bt.open {0|1}, 106
Bt.read.bytes <svar>, 108
Bt.read.ready <nvar>, 108
Bt.reconnect, 107
Bt.set.UUID <sexp>, 109
Bt.status {{<connect_var>}{, <name_svar>}{, <address_svar>}}, 107
Bt.write {<exp> {,|;}} ..., 108
Bundle.clear <pointer_nexp>, 45
Bundle.contain <pointer_nexp>, <key_sexp> , <contains_nvar>, 44
Bundle.create <pointer_nvar>, 43
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 44
Bundle.keys <bundle_ptr_nexp>, <list_ptr_nexp>, 44
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 43
Bundle.remove <pointer_nexp>, <key_sexp>, 45
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 44
BXOR(<nexp1>, <nexp2>), 51
Byte.close <file_table_nexp>, 95
Byte.copy <file_table_nexp>,<output_file_sexp>, 96
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 94
Byte.position.get <file_table_nexp>, <position_nexp>, 95
Byte.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 96
Byte.position.set <file_table_nexp>, <position_nexp>, 96
Byte.read.buffer <file_table_nexp>, <count_nexp>, <buffer_svar>, 95
Byte.read.byte <file_table_nexp>, <byte_nvar>, 95
Byte.write.buffer <file_table_nexp>, <sexp>, 95
Byte.write.byte <file_table_nexp>, <byte_nexp>|<sexp>, 95
Call <user_defined_function>, 67
CBRT(<nexp>), 51
CEIL(<nexp>), 52
CHR$ (<nexp>, ...), 58
Clipboard.get <svar>, 109
Clipboard.put <sexp>, 109
CLOCK(), 57
Cls, 81
Console.line. count <count_nvar >, 82
Console.line.text <line_nexp>, <text_svar>, 82
Console.line.touched <line_nvar> {, <press_lvar>}, 83
Console.save <filename_sexp>, 83
Console.title { <title_sexp>}, 81
ConsoleTouch.resume, 75
COS(<nexp>), 54
COSH(<nexp>), 54
D_U.break, 70
D_U.continue, 70
Debug.dump.array Array[], 78
Debug.dump.bundle <bundlePtr_nexp>, 78
Debug.dump.list <listPtr_nexp>, 78
Debug.dump.scalars, 78
Debug.dump.stack <stackPtr_nexp>, 78
Debug.echo.off, 77
Debug.echo.on, 77
Debug.off, 77
Debug.on, 77
Debug.print, 77
Debug.show, 80
Debug.show.array Array[], 78
Debug.show.bundle <bundlePtr_nexp>, 79
Debug.show.list <listPtr_nexp>, 79
Debug.show.program, 80
Debug.show.scalars, 78
Debug.show.stack <stackPtr_nexp>, 79
Debug.show.watch, 79
Debug.watch var, ..., 79
Decrypt <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 109
Device <nvar>|<nexp>, 114
Device <svar>, 114
Dialog.message {<title_sexp>}, {<message_sexp>}, <selection_nvar> {, <button1_sexp>{, <button2_sexp>{, <button3_sexp>}}}, 83
Dialog.select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp>}, 84
Dim Array [n, n, ...], Array$[n, n, ...] ..., 36
Do / Until <lexp>, 70
Echo.off, 109
Echo.on, 109
Email.send <recipient_sexp>, <subject_sexp>, <body_sexp>, 121
Encrypt <pw_sexp>, <source_sexp>, <encrypted_svar>, 109
End{ <msg_sexp>}, 76
ENDS_WITH(<sub_sexp>, <base_sexp>), 56
Exit, 76
EXP(<nexp>), 53
F_N.break, 69
F_N.continue, 69
File.delete <lvar>, <path_sexp>, 90
File.dir <path_sexp>, Array[] {, <dirmark_sexp>}, 90
File.exists <lvar>, <path_sexp>, 90
File.mkdir <path_sexp>, 90
File.rename <old_path_sexp>, <new_path_sexp>, 90
File.root <svar>, 91
File.size <size_nvar>, <path_sexp>, 91
File.type <type_svar>, <path_sexp>, 91
FLOOR(<nexp>), 52
Fn.def name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 66
Fn.end, 67
Fn.rtn <sexp>|<nexp>, 67
Font.clear, 81
Font.delete {<font_ptr_nexp>}, 80
Font.load <font_ptr_nvar>, <filename_sexp>, 80
For - To - Step / Next, 68
FORMAT$(<pattern_sexp>, <nexp>), 64
FORMAT_USING$(<locale_sexp>, <format_sexp> { , <exp>}...), 64
FRAC(<nexp>), 52
Ftp.cd <new_directory_sexp>, 105
Ftp.close, 104
Ftp.delete <filename_sexp>, 105
Ftp.dir <list_nvar>, 105
Ftp.get <source_sexp>, <destination_sexp>, 104
Ftp.mkdir <directory_sexp>, 105
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 104
Ftp.put <source_sexp>, <destination_sexp>, 104
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 105
Ftp.rmdir <directory_sexp>, 105
GETERROR$(), 57
GoSub <index_nexp>, <label>... / Return, 72
GoSub <label> / Return, 71
GoTo <index_nexp>, <label>..., 71
GoTo <label>, 71
Gps.accuracy <nvar>, 157
Gps.altitude <nvar>, 157
Gps.bearing <nvar>, 157
Gps.close, 154
Gps.latitude <nvar>, 157
Gps.location {{<time_nvar>}, {<prov_svar>}, {<count_nvar}, {<acc_nvar>}, {<lat_nvar>}, {<long_nvar>}, {<bear_nvar>}, {<speed_nvar>}}, 156
Gps.longitude <nvar>, 157
Gps.open {{<status_nvar>},{<time_nexp>},{<distance_nexp>}}, 153
Gps.provider <svar>, 157
Gps.satellites {{<count_nvar>}, {<sat_list_nexp>}}, 157
Gps.speed <nvar>, 157
Gps.status {{<status_var>}, {<infix_nvar>},{inview_nvar}, {<sat_list_nexp>}}, 154
Gps.time <nvar>, 157
Gr.arc <object_number_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 132
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 139
Gr.bitmap.crop <new_bitmap_object_nvar>, <source_bitmap_object_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 140
Gr.bitmap.delete <bitmap_ptr_nvar>, 140
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nvar>, x , y, 141
Gr.bitmap.drawinto.end, 141
Gr.bitmap.drawinto.start <bitmap_ptr_nvar>, 141
Gr.bitmap.load <bitmap_ptr_nvar>, <file_name_sexp>, 139
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 141
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nvar>, width, height {, <smoothing_lexp> }, 140
Gr.bitmap.size <bitmap_ptr_nexp>, width, height, 140
Gr.bounded.touch touched, left, top, right, bottom, 135
Gr.bounded.touch2 touched, left, top, right, bottom, 135
Gr.brightness <nexp>, 131
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 143
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 144
Gr.camera.select 1|2, 142
Gr.camera.shoot <bm_ptr_nvar>, 143
Gr.circle <object_number_nvar>, x, y, radius, 133
Gr.clip <object__ptr_nvar>, <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 147
Gr.close, 131
Gr.cls, 130
Gr.color alpha, red, green, blue, <style_nexp>, 128
Gr.front flag, 131
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 141
Gr.get.params <object_ptr_nvar>, <param_array$[]>, 144
Gr.get.pixel x, y, alpha, red, green, blue, 144
Gr.get.position <object_ptr_nvar>, x, y, 144
Gr.get.textbounds <sexp>, left, top, right, bottom, 136
Gr.get.type <object_ptr_nvar>, <type_svar>, 144
Gr.get.value <object_ptr_nvar>, <tag_sexp>, {<value_nvar | value_svar>}, 145
Gr.getDL <dl_array[]> {, <keep_all_objects_lexp> }, 148
Gr.hide <object_number_nvar>, 134
Gr.line <object_number_nvar>, x1, y1, x2, y2, 132
Gr.modify <object_ptr_nvar>, {<tag_sexp>, <value_nvar | value_svar>}, ..., 145
Gr.newDL <dl_array[{<start>,<length>}]>, 148
Gr.onGrTouch.resume, 135
Gr.open alpha, red, green, blue {, <ShowStatusBar_lexp> {, <Orientation_nexp>}}, 128
Gr.orientation <nexp>, 129
Gr.oval <object_number_nvar>, left, top, right, bottom, 132
Gr.paint.get <object_ptr_nvar>, 146
Gr.point <object_number_nvar>, x, y, 131
Gr.poly <object_number_nvar>, list_pointer {,x,y}, 133
Gr.rect <object_number_nvar>, left, top, right, bottom, 132
Gr.render, 130
Gr.rotate.end {<obj_nvar>}, 142
Gr.rotate.start angle, x, y{,<obj_nvar>}, 141
Gr.save <filename_sexp> {,<quality_nexp>}, 144
Gr.scale x_factor, y_factor, 130
Gr.screen width, height{, density }, 130
Gr.screen.to_bitmap <bm_ptr_nvar>, 144
Gr.set.antialias <lexp>, 129
Gr.set.pixels <object_number_nvar>, pixels[{<start>,<length>}] {,x,y}, 133
Gr.set.stroke <nexp>, 129
Gr.show <object_number_nvar>, 134
Gr.statusbar {<height_nvar>} {, showing_lvar}, 129
Gr.statusbar.show <nexp>, 130
Gr.text.align type, 135
Gr.text.bold <lexp>, 138
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 139
Gr.text.height {<height_nvar>} {, <up_nvar>} {, <down_nvar>}, 136
Gr.text.setfont {<font_ptr_nexp>|<font_family_sexp>} {, <style_sexp>}, 137
Gr.text.size <nexp>, 136
Gr.text.skew <nexp>, 138
Gr.text.strike <lexp>, 138
Gr.text.typeface {<nexp>} {, <style_nexp>}, 138
Gr.text.underline <lexp>, 138
Gr.text.width <nvar>, <sexp>, 136
Gr.touch touched, x, y, 134
Gr.touch2 touched, x, y, 135
GR_COLLISION ( <object_1_nvar>, <object_2_nvar>), 56
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 94
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 94
Headset <state_nvar>, <type_svar>, <mic_nvar>, 121
HEX$(<nexp>), 59
HEX(<sexp>), 55
Home, 122
Html.clear.cache, 99
Html.clear.history, 99
Html.close, 99
Html.get.datalink <data_svar>, 98
Html.go.back, 99
Html.go.forward, 99
Html.load.string <html_sexp>, 98
Html.load.url <file_sexp>, 97
Html.open {<ShowStatusBar_lexp> {, <Orientation_nexp>}}, 97
Html.orientation <nexp>, 97
Html.post <url_sexp>, <list_nexp>, 98
Http.post <url_sexp>, <list_nexp>, <result_svar>, 100
HYPOT(<nexp_x>, <nexp_y), 53
If / Then / Else, 68
If / Then / Else / Elseif / Endif, 68
Include FilePath, 72
Inkey$ <svar>, 85
Input {<prompt_sexp>}, <result_var>{, <default_exp>},{<canceled_nvar>}, 84
INT$(<nexp>), 59
INT(<nexp>), 52
IS_IN(<sub_sexp>, <base_sexp>{, <start_nexp>}, 55
Kb.hide, 87
Kb.toggle, 87
Key.resume, 76
LEFT$ (<sexp>, <nexp>), 58
LEN(<sexp>), 55
Let, 49
List.add <pointer_nexp>, <nexp>{,<nexp>...,<nexp>}, 40
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 41
List.add.array string_list_pointer, Array$[{<start>,<length>}], 41
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 41
List.clear <pointer_nexp>, 42
List.create N|S, <pointer_nvar>, 40
List.get <pointer_nexp>,<index_nexp>, <svar>|<nvar>, 41
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 41
List.remove <pointer_nexp>,<index_nexp>, 41
List.replace <pointer_nexp>,<index_nexp>, <sexp>|<nexp>, 41
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 42
List.size <pointer_nexp>, <nvar>, 42
List.toArray <pointer_nexp>, Array$[] | Array[], 42
List.type <pointer_nexp>, <svar>, 41
LOG(<nexp>), 53
LOG10(<nexp>), 53
LOWER$(<sexp>), 59
MAX(<nexp>, <nexp>), 52
MenuKey.resume, 76
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 58
MIN(<nexp>, <nexp>), 52
MOD(<nexp1>, <nexp2>), 52
MyPhoneNumber <svar>, 120
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 122
OCT$(<nexp>, 59
OCT(<sexp>), 55
OnBackGround:, 122
OnBackKey:, 75
OnBtReadReady:, 108
OnConsoleTouch:, 75
OnError:, 74
OnGrTouch:, 135
OnKeyPress:, 76
OnMenuKey:, 75
OnTimer:, 113
Pause <ticks_nexp>, 116
Phone.call <sexp>, 120
Phone.info <nvar>|<nexp>, 115
Phone.rcv.init, 120
Phone.rcv.next <state_nvar>, <number_svar>, 120
PI(), 54
Popup <message_sexp>, <x_nexp>, <y_nexp>, <duration_nexp>, 85
POW(<nexp1>, <nexp2>), 53
Print {<exp> {,|;}} ..., 81
RANDOMIZE(<nexp>), 51
Read.data <number>|<string> {,<number>|<string>...,<number>|<string>}, 76
Read.from <nexp>, 77
Read.next <var>, ..., 76
Rem, 47
REPLACE$( <target_sexp>, <argument_sexp>, <replace_sexp>), 58
RIGHT$(<sexp>, <nexp>), 58
Ringer.get.mode <nvar>, 110
Ringer.get.volume <vol_nvar> { , <max_nvar>, 110
Ringer.set.mode <nexp>, 110
Ringer.set.volume <nexp>, 110
RND(), 52
ROUND(<value_nexp>{, <count_nexp>{, <mode_sexp>}}), 52
Run <filename_sexp> {, <data_sexp>}, 73
Select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 86
Sensors.close, 159
Sensors.list <sensor_array$[]>, 158
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 159
Sensors.read sensor_type, p1, p2, p3, 159
SGN(<nexp>), 51
SHIFT (<value_nexp>, <bits_nexp>), 55
SIN(<nexp>), 54
SINH(<nexp>), 54
Sms.rcv.init, 121
Sms.rcv.next <svar>, 121
Sms.send <number_sexp>, <message_sexp>, 121
Socket.client.close, 102
Socket.client.connect <server_sexp>, <port_nexp> { , <wait_lexp> }, 101
Socket.client.read.file <fw_nexp>, 101
Socket.client.read.line <line_svar>, 101
Socket.client.read.ready <nvar>, 101
Socket.client.server.ip <svar>, 101
Socket.client.status <status_nvar>, 101
Socket.client.write.bytes <sexp>, 102
Socket.client.write.file <fr_nexp>, 102
Socket.client.write.line <line_sexp>, 102
Socket.myIP <svar>, 102
Socket.server.client.ip <nvar>, 104
Socket.server.close, 104
Socket.server.connect {<wait_lexp>}, 102
Socket.server.create <port_nexp>, 102
Socket.server.disconnect, 104
Socket.server.read.file <fw_nexp>, 104
Socket.server.read.line <svar>, 103
Socket.server.read.ready <nvar>, 103
Socket.server.status <status_nvar>, 103
Socket.server.write.bytes <sexp>, 103
Socket.server.write.file <fr_nexp>, 103
Socket.server.write.line <sexp>, 103
Soundpool.load <soundID_nvar>, <file_path_sexp>, 151
Soundpool.open <MaxStreams_nexp>, 151
Soundpool.pause <streamID_nexp>, 152
Soundpool.play <streamID_nvar>, <sounded_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 152
Soundpool.release, 152
Soundpool.resume <streamID_nexp>, 152
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 152
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 152
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 152
Soundpool.stop <streamID_nexp>, 152
Soundpool.unload <soundID_nexp>, 151
Split <result_array$[]>, <source_sexp> {, <test_sexp>}, 116
Split.all <result_array$[]>, <source_sexp> {, <test_sexp>}, 116
Sql.close <DB_pointer_nvar>, 123
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 125
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 124
Sql.exec <DB_pointer_nvar>, <command_sexp>, 126
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$, ...,CN$, VN$, 124
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 123
Sql.next <done_lvar>, <cursor_nvar>, C1V$, C2V$, .., CNV$, 125
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 123
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp>, <order_sexp> } }, 124
Sql.query.length <length_nvar>, <cursor_nvar>, 125
Sql.query.position <position_nvar>, <cursor_nvar>, 125
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 126
Sql.update <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN$: <where_sexp>, 126
SQR(<nexp>), 51
Stack.clear <ptr_nexp>, 46
Stack.create N|S, <ptr_nvar>, 45
Stack.isEmpty <ptr_nexp>, <nvar>, 46
Stack.peek <ptr_nexp>, <nvar>|<svar>, 46
Stack.pop <ptr_nexp>, <nvar>|<svar>, 46
Stack.push <ptr_nexp>, <nexp>|<sexp>, 46
Stack.type <ptr_nexp>, <svar>, 46
STARTS_WITH (<sub_sexp>, <base_sexp>{, <start_nexp>}, 56
STR$(<nexp>), 59
Stt.listen, 111
Stt.results <string_list_ptr_nvar>, 112
Su.close, 160
Su.open, 160
Su.read.line <svar>, 160
Su.read.ready <nvar>, 160
Su.write <sexp>, 160
Sw.begin <nexp>|<sexp>, 74
Sw.break, 74
Sw.case <nexp >|<sexp>, 74
Sw.default, 74
Sw.end, 74
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 117
System.close, 160
System.open, 159
System.read.line <svar>, 160
System.read.ready <nvar>, 159
System.write <sexp>, 159
TAN(<nexp>), 54
Text.close <file_table_nexp>, 92
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 86
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 91
Text.position.get <file_table_nexp>, <position_nvar>, 93
Text.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 93
Text.position.set <file_table_nexp>, <position_nexp>, 93
Text.readln <file_table_nexp>, <line_svar>, 92
Text.writeln <file_table_nexp>, {<exp> {,|;}} ..., 92
TGet <result_svar>, <prompt_sexp> {, <title_sexp>}, 87
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 117
TIME(), 57
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 57
Timer.clear, 113
Timer.resume, 113
Timer.set <interval_nexp>, 113
TimeZone.get <tz_svar>, 118
TimeZone.list <tz_list_pointer_nexp>, 118
TimeZone.set { <tz_sexp> }, 118
TODEGREES(<nexp>), 54
Tone <frequency_nexp>, <duration_nexp>, 118
TORADIANS(<nexp>), 54
Tts.init, 111
Tts.speak <sexp> {, <wait_lexp>}, 111
Tts.speak.toFile <sexp> {, <path_sexp>}, 111
Tts.stop, 111
UCODE(<sexp>{, <index_nexp>}), 55
UnDim Array[], Array$[], ..., 36
UPPER$(<sexp>), 59
USING$({<locale_sexp>} , <format_sexp> { , <exp>}...), 59
VAL(<sexp>), 55
VERSION$(), 59
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 118
W_R.break, 70
W_R.continue, 70
WakeLock <code_nexp>, 119
While <lexp> / Repeat, 69
WiFi.info {{<SSID_svar>}{, <BSSID_svar>}{, <MAC_svar>}{, <IP_var>}{, <speed_nvar>}}, 120
WifiLock <code_nexp>, 119
WORD$(<source_sexp>, <n_nexp> {, <test_sexp>}), 58
