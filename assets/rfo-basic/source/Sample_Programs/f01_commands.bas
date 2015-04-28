 There is 200+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 47
!! - Block Comment, 48
# - Format Line, 25
% - Middle of Line Comment, 48
? {<exp> {,|;}} ..., 84
ABS(<nexp>), 52
ACOS(<nexp>), 55
Array.average <Average_nvar>, Array[{<start>,<length>}], 37
Array.copy SourceArray$[{<start>,<length>}], DestinationArray$[{{-}<extras>}], 37
Array.copy SourceArray[{<start>,<length>}], DestinationArray[{{-}<extras>}], 37
Array.delete Array[], Array$[] ..., 37
Array.length n, Array$[{<start>,<length>}], 37
Array.length n, Array[{<start>,<length>}], 37
Array.load Array$[], <sexp>, ..., 38
Array.load Array[], <nexp>, ..., 38
Array.max <max_nvar> Array[{<start>,<length>}], 38
Array.min <min_nvar>, Array[{<start>,<length>}], 38
Array.reverse Array$[{<start>,<length>}], 38
Array.reverse Array[{<start>,<length>}], 38
Array.search Array$[{<start>,<length>}], <value_sexp>, <result_nvar>{,<start_nexp>}, 38
Array.search Array[{<start>,<length>}], <value_nexp>, <result_nvar>{,<start_nexp>}, 38
Array.shuffle Array[{<start>,<length>}], 38
Array.sort Array[{<start>,<length>}], 39
Array.std_dev <sd_nvar>, Array[{<start>,<length>}], 39
Array.sum <sum_nvar>, Array[{<start>,<length>}], 39
Array.variance <v_nvar>, Array[{<start>,<length>}], 39
ASCII(<sexp>{, <index_nexp>}), 56
ASIN(<nexp>), 55
ATAN(<nexp>), 55
ATAN2 (<nexp_y>, <nexp_x>), 55
Audio.isdone <lvar>, 157
Audio.length <length_nvar>, <aft_nexp>, 157
Audio.load <aft_nvar>, <filename_sexp>, 155
Audio.loop, 156
Audio.pause, 156
Audio.play <aft_nexp>, 156
Audio.position.current <nvar>, 157
Audio.position.seek <nexp>, 157
Audio.record.start <fn_svar>, 157
Audio.record.stop, 157
Audio.release <aft_nexp>, 157
Audio.stop, 156
Audio.volume <left_nexp>, <right_nexp>, 156
Back.resume, 78
BACKGROUND(), 57
Background.resume, 126
BAND(<nexp1>, <nexp2>), 51
BIN$(<nexp>), 61
BIN(<sexp>), 56
BNOT(<nexp>, 51
BOR(<nexp1>, <nexp2>), 51
Browse <url_sexp>, 102
Bt.close, 110
Bt.connect {0|1}, 110
Bt.device.name <svar>, 111
Bt.disconnect, 110
Bt.onReadReady.resume, 111
Bt.open {0|1}, 109
Bt.read.bytes <svar>, 111
Bt.read.ready <nvar>, 111
Bt.reconnect, 110
Bt.set.UUID <sexp>, 112
Bt.status {{<connect_var>}{, <name_svar>}{, <address_svar>}}, 110
Bt.write {<exp> {,|;}} ..., 111
Bundle.clear <pointer_nexp>, 46
Bundle.contain <pointer_nexp>, <key_sexp> , <contains_nvar>, 45
Bundle.create <pointer_nvar>, 44
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 44
Bundle.keys <bundle_ptr_nexp>, <list_ptr_nexp>, 44
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 44
Bundle.remove <pointer_nexp>, <key_sexp>, 45
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 45
BXOR(<nexp1>, <nexp2>), 51
Byte.close <file_table_nexp>, 97
Byte.copy <file_table_nexp>,<output_file_sexp>, 99
Byte.eof <file_table_nexp>, <lvar>, 98
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 97
Byte.position.get <file_table_nexp>, <position_nexp>, 98
Byte.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 99
Byte.position.set <file_table_nexp>, <position_nexp>, 98
Byte.read.buffer <file_table_nexp>, <count_nexp>, <buffer_svar>, 98
Byte.read.byte <file_table_nexp>, <byte_nvar>, 97
Byte.truncate <file_table_nexp>,<length_nexp>, 99
Byte.write.buffer <file_table_nexp>, <sexp>, 98
Byte.write.byte <file_table_nexp>, <byte_nexp>|<sexp>, 98
Call <user_defined_function>, 69
CBRT(<nexp>), 52
CEIL(<nexp>), 53
CHR$ (<nexp>, ...), 58
Clipboard.get <svar>, 116
Clipboard.put <sexp>, 116
CLOCK(), 58
Cls, 83
Console.line.count <count_nvar >, 85
Console.line.text <line_nexp>, <text_svar>, 85
Console.line.touched <line_nvar> {, <press_lvar>}, 85
Console.save <filename_sexp>, 85
Console.title { <title_sexp>}, 83
ConsoleTouch.resume, 77
COS(<nexp>), 54
COSH(<nexp>), 55
D_U.break, 72
D_U.continue, 72
Debug.dump.array Array[], 80
Debug.dump.bundle <bundlePtr_nexp>, 80
Debug.dump.list <listPtr_nexp>, 80
Debug.dump.scalars, 80
Debug.dump.stack <stackPtr_nexp>, 80
Debug.echo.off, 80
Debug.echo.on, 80
Debug.off, 79
Debug.on, 79
Debug.print, 80
Debug.show, 82
Debug.show.array Array[], 81
Debug.show.bundle <bundlePtr_nexp>, 81
Debug.show.list <listPtr_nexp>, 81
Debug.show.program, 82
Debug.show.scalars, 80
Debug.show.stack <stackPtr_nexp>, 81
Debug.show.watch, 82
Debug.watch var, ..., 82
Decrypt <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 116
Device <nvar>|<nexp>, 121
Device <svar>, 121
Dialog.message {<title_sexp>}, {<message_sexp>}, <selection_nvar> {, <button1_sexp>{, <button2_sexp>{, <button3_sexp>}}}, 86
Dialog.select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp>}, 86
Dim Array [n, n, ...], Array$[n, n, ...] ..., 36
Do / Until <lexp>, 72
Echo.off, 116
Echo.on, 116
Email.send <recipient_sexp>, <subject_sexp>, <body_sexp>, 112
Encrypt <pw_sexp>, <source_sexp>, <encrypted_svar>, 116
End{ <msg_sexp>}, 78
ENDS_WITH(<sub_sexp>, <base_sexp>), 57
Exit, 78
EXP(<nexp>), 54
F_N.break, 71
F_N.continue, 71
File.delete <lvar>, <path_sexp>, 92
File.dir <path_sexp>, Array[] {, <dirmark_sexp>}, 92
File.exists <lvar>, <path_sexp>, 92
File.mkdir <path_sexp>, 93
File.rename <old_path_sexp>, <new_path_sexp>, 93
File.root <svar>, 93
File.size <size_nvar>, <path_sexp>, 93
File.type <type_svar>, <path_sexp>, 93
FLOOR(<nexp>), 53
Fn.def name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 68
Fn.end, 69
Fn.rtn <sexp>|<nexp>, 69
Font.clear, 83
Font.delete {<font_ptr_nexp>}, 83
Font.load <font_ptr_nvar>, <filename_sexp>, 83
For - To - Step / Next, 71
FORMAT$(<pattern_sexp>, <nexp>), 65
FORMAT_USING$(<locale_sexp>, <format_sexp> { , <exp>}...), 65
FRAC(<nexp>), 53
Ftp.cd <new_directory_sexp>, 108
Ftp.close, 107
Ftp.delete <filename_sexp>, 108
Ftp.dir <list_nvar>, 108
Ftp.get <source_sexp>, <destination_sexp>, 107
Ftp.mkdir <directory_sexp>, 108
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 107
Ftp.put <source_sexp>, <destination_sexp>, 107
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 108
Ftp.rmdir <directory_sexp>, 108
GETERROR$(), 58
GoSub <index_nexp>, <label>... / Return, 74
GoSub <label> / Return, 74
GoTo <index_nexp>, <label>..., 73
GoTo <label>, 73
Gps.accuracy <nvar>, 164
Gps.altitude <nvar>, 164
Gps.bearing <nvar>, 164
Gps.close, 160
Gps.latitude <nvar>, 164
Gps.location {{<time_nvar>}, {<prov_svar>}, {<count_nvar}, {<acc_nvar>}, {<lat_nvar>}, {<long_nvar>}, {<alt_nvar>}, {<bear_nvar>}, {<speed_nvar>}}, 163
Gps.longitude <nvar>, 164
Gps.open {{<status_nvar>},{<time_nexp>},{<distance_nexp>}}, 160
Gps.provider <svar>, 163
Gps.satellites {{<count_nvar>}, {<sat_list_nexp>}}, 163
Gps.speed <nvar>, 164
Gps.status {{<status_var>}, {<infix_nvar>},{inview_nvar}, {<sat_list_nexp>}}, 160
Gps.time <nvar>, 163
Gr.arc <object_number_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 137
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 145
Gr.bitmap.crop <new_bitmap_object_nvar>, <source_bitmap_object_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 146
Gr.bitmap.delete <bitmap_ptr_nexp>, 146
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nexp>, x , y, 147
Gr.bitmap.drawinto.end, 147
Gr.bitmap.drawinto.start <bitmap_ptr_nexp>, 147
Gr.bitmap.load <bitmap_ptr_nvar>, <file_name_sexp>, 145
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 147
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nexp>, width, height {, <smoothing_lexp> }, 146
Gr.bitmap.size <bitmap_ptr_nexp>, width, height, 146
Gr.bounded.touch touched, left, top, right, bottom, 141
Gr.bounded.touch2 touched, left, top, right, bottom, 141
Gr.brightness <nexp>, 136
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 149
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 150
Gr.camera.select 1|2, 149
Gr.camera.shoot <bm_ptr_nvar>, 149
Gr.circle <object_number_nvar>, x, y, radius, 137
Gr.clip <object__ptr_nexp>, <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 154
Gr.close, 135
Gr.cls, 135
Gr.color {{alpha}{, red}{, green}{, blue}{, style}}, 133
Gr.front flag, 135
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 147
Gr.get.params <object_ptr_nexp>, <param_array$[]>, 151
Gr.get.pixel x, y, alpha, red, green, blue, 150
Gr.get.position <object_ptr_nexp>, x, y, 151
Gr.get.textbounds <sexp>, left, top, right, bottom, 143
Gr.get.type <object_ptr_nexp>, <type_svar>, 150
Gr.get.value <object_ptr_nexp>, <tag_sexp>, {<value_nvar | value_svar>}, 151
Gr.getDL <dl_array[]> {, <keep_all_objects_lexp> }, 155
Gr.group <object_number_nvar>{, <obj_nexp>}..., 139
Gr.group.getDL <object_number_nvar>, 139
Gr.group.list <object_number_nvar>, <list_ptr_nexp>, 139
Gr.group.newDL <object_number_nvar>, 140
Gr.hide <object_number_nexp>, 140
Gr.line <object_number_nvar>, x1, y1, x2, y2, 136
Gr.modify <object_ptr_nexp>, {<tag_sexp>, <value_nvar | value_svar>}, ..., 151
Gr.move <object_ptr_nexp>{{, dx}{, dy}}, 151
Gr.newDL <dl_array[{<start>,<length>}]>, 155
Gr.onGrTouch.resume, 141
Gr.open {{alpha}{, red}{, green}{, blue}{, <ShowStatusBar_lexp>}{, <Orientation_nexp>}}, 132
Gr.orientation <nexp>, 134
Gr.oval <object_number_nvar>, left, top, right, bottom, 137
Gr.paint.get <object_ptr_nvar>, 153
Gr.point <object_number_nvar>, x, y, 136
Gr.poly <object_number_nvar>, list_pointer {,x,y}, 138
Gr.rect <object_number_nvar>, left, top, right, bottom, 136
Gr.render, 134
Gr.rotate.end {<obj_nvar>}, 148
Gr.rotate.start angle, x, y{,<obj_nvar>}, 148
Gr.save <filename_sexp> {,<quality_nexp>}, 150
Gr.scale x_factor, y_factor, 135
Gr.screen width, height{, density }, 134
Gr.screen.to_bitmap <bm_ptr_nvar>, 150
Gr.set.antialias <lexp>, 133
Gr.set.pixels <object_number_nvar>, pixels[{<start>,<length>}] {,x,y}, 138
Gr.set.stroke <nexp>, 133
Gr.show <object_number_nexp>, 140
Gr.show.toggle <object_number_nexp>, 140
Gr.statusbar {<height_nvar>} {, showing_lvar}, 134
Gr.statusbar.show <nexp>, 134
Gr.text.align type, 142
Gr.text.bold <lexp>, 144
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 145
Gr.text.height {<height_nvar>} {, <up_nvar>} {, <down_nvar>}, 142
Gr.text.setfont {<font_ptr_nexp>|<font_family_sexp>} {, <style_sexp>}, 143
Gr.text.size <nexp>, 142
Gr.text.skew <nexp>, 144
Gr.text.strike <lexp>, 145
Gr.text.typeface {<nexp>} {, <style_nexp>}, 144
Gr.text.underline <lexp>, 144
Gr.text.width <nvar>, <sexp>, 143
Gr.touch touched, x, y, 140
Gr.touch2 touched, x, y, 141
GR_COLLISION ( <object_1_nvar>, <object_2_nvar>), 57
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 96
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 96
Headset <state_nvar>, <type_svar>, <mic_nvar>, 122
HEX$(<nexp>), 61
HEX(<sexp>), 55
Home, 126
Html.clear.cache, 102
Html.clear.history, 102
Html.close, 102
Html.get.datalink <data_svar>, 101
Html.go.back, 102
Html.go.forward, 102
Html.load.string <html_sexp>, 101
Html.load.url <file_sexp>, 100
Html.open {<ShowStatusBar_lexp> {, <Orientation_nexp>}}, 100
Html.orientation <nexp>, 100
Html.post <url_sexp>, <list_nexp>, 101
Http.post <url_sexp>, <list_nexp>, <result_svar>, 103
HYPOT(<nexp_x>, <nexp_y), 54
If / Then / Else, 70
If / Then / Else / Elseif / Endif, 70
Include FilePath, 75
Inkey$ <svar>, 87
Input {<prompt_sexp>}, <result_var>{, {<default_exp>}{,<canceled_nvar>}}, 87
INT$(<nexp>), 60
INT(<nexp>), 53
IS_IN(<sub_sexp>, <base_sexp>{, <start_nexp>}, 56
Join <source_array$[]>, <result_svar> {, <separator_sexp>{, <wrapper_sexp}}, 117
Join.all <source_array$[]>, <result_svar> {, <separator_sexp>{, <wrapper_sexp}}, 117
Kb.hide, 89
Kb.toggle, 89
Key.resume, 78
LEFT$ (<sexp>, <count_nexp>), 59
LEN(<sexp>), 55
Let, 50
List.add <pointer_nexp>{, <exp>}..., 41
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 41
List.add.array string_list_pointer, Array$[{<start>,<length>}], 41
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 41
List.clear <pointer_nexp>, 42
List.create N|S, <pointer_nvar>, 41
List.get <pointer_nexp>,<index_nexp>, <svar>|<nvar>, 42
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 42
List.remove <pointer_nexp>,<index_nexp>, 42
List.replace <pointer_nexp>,<index_nexp>, <sexp>|<nexp>, 42
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 43
List.size <pointer_nexp>, <nvar>, 42
List.toArray <pointer_nexp>, Array$[] | Array[], 43
List.type <pointer_nexp>, <svar>, 42
LOG(<nexp>), 54
LOG10(<nexp>), 54
LOWER$(<sexp>), 60
MAX(<nexp>, <nexp>), 52
MenuKey.resume, 78
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 59
MIN(<nexp>, <nexp>), 52
MOD(<nexp1>, <nexp2>), 53
MyPhoneNumber <svar>, 112
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 122
OCT$(<nexp>, 61
OCT(<sexp>), 56
OnBackGround:, 126
OnBackKey:, 77
OnBtReadReady:, 111
OnConsoleTouch:, 77
OnError:, 77
OnGrTouch:, 141
OnKeyPress:, 78
OnMenuKey:, 78
OnTimer:, 115
Pause <ticks_nexp>, 124
Phone.call <sexp>, 112
Phone.dial <sexp>, 112
Phone.info <nvar>|<nexp>, 123
Phone.rcv.init, 112
Phone.rcv.next <state_nvar>, <number_svar>, 112
PI(), 54
Popup <message_sexp>{{, <x_nexp>}{, <y_nexp>}{, <duration_lexp>}}, 88
POW(<nexp1>, <nexp2>), 54
Print {<exp> {,|;}} ..., 84
RANDOMIZE(<nexp>), 52
Read.data <number>|<string> {,<number>|<string>...,<number>|<string>}, 79
Read.from <nexp>, 79
Read.next <var>, ..., 79
Rem, 48
REPLACE$( <target_sexp>, <argument_sexp>, <replace_sexp>), 60
RIGHT$(<sexp>, <count_nexp>), 59
Ringer.get.mode <nvar>, 117
Ringer.get.volume <vol_nvar> { , <max_nvar>, 117
Ringer.set.mode <nexp>, 117
Ringer.set.volume <nexp>, 117
RND(), 52
ROUND(<value_nexp>{, <count_nexp>{, <mode_sexp>}}), 53
Run <filename_sexp> {, <data_sexp>}, 75
Select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 88
Sensors.close, 166
Sensors.list <sensor_array$[]>, 165
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 165
Sensors.read sensor_type, p1, p2, p3, 166
SGN(<nexp>), 52
SHIFT (<value_nexp>, <bits_nexp>), 56
SIN(<nexp>), 54
SINH(<nexp>), 55
Sms.rcv.init, 113
Sms.rcv.next <svar>, 113
Sms.send <number_sexp>, <message_sexp>, 113
Socket.client.close, 105
Socket.client.connect <server_sexp>, <port_nexp> { , <wait_lexp> }, 104
Socket.client.read.file <fw_nexp>, 104
Socket.client.read.line <line_svar>, 104
Socket.client.read.ready <nvar>, 104
Socket.client.server.ip <svar>, 104
Socket.client.status <status_nvar>, 104
Socket.client.write.bytes <sexp>, 105
Socket.client.write.file <fr_nexp>, 105
Socket.client.write.line <line_sexp>, 105
Socket.myIP <svar>, 105
Socket.server.client.ip <nvar>, 107
Socket.server.close, 107
Socket.server.connect {<wait_lexp>}, 105
Socket.server.create <port_nexp>, 105
Socket.server.disconnect, 107
Socket.server.read.file <fw_nexp>, 107
Socket.server.read.line <svar>, 106
Socket.server.read.ready <nvar>, 106
Socket.server.status <status_nvar>, 106
Socket.server.write.bytes <sexp>, 106
Socket.server.write.file <fr_nexp>, 106
Socket.server.write.line <sexp>, 106
Soundpool.load <soundID_nvar>, <file_path_sexp>, 158
Soundpool.open <MaxStreams_nexp>, 158
Soundpool.pause <streamID_nexp>, 159
Soundpool.play <streamID_nvar>, <soundID_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 158
Soundpool.release, 159
Soundpool.resume <streamID_nexp>, 159
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 159
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 159
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 159
Soundpool.stop <streamID_nexp>, 159
Soundpool.unload <soundID_nexp>, 158
Split <result_array$[]>, <source_sexp> {, <test_sexp>}, 118
Split.all <result_array$[]>, <source_sexp> {, <test_sexp>}, 118
Sql.close <DB_pointer_nvar>, 127
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 129
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 128
Sql.exec <DB_pointer_nvar>, <command_sexp>, 130
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$, ...,CN$, VN$, 128
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 127
Sql.next <done_lvar>, <cursor_nvar>, C1V$, C2V$, .., CNV$, 129
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 127
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp>, <order_sexp> } }, 128
Sql.query.length <length_nvar>, <cursor_nvar>, 129
Sql.query.position <position_nvar>, <cursor_nvar>, 129
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 130
Sql.update <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN${: <where_sexp>, 130
SQR(<nexp>), 52
Stack.clear <ptr_nexp>, 47
Stack.create N|S, <ptr_nvar>, 46
Stack.isEmpty <ptr_nexp>, <nvar>, 47
Stack.peek <ptr_nexp>, <nvar>|<svar>, 46
Stack.pop <ptr_nexp>, <nvar>|<svar>, 46
Stack.push <ptr_nexp>, <nexp>|<sexp>, 46
Stack.type <ptr_nexp>, <svar>, 47
STARTS_WITH (<sub_sexp>, <base_sexp>{, <start_nexp>}, 57
STR$(<nexp>), 60
Stt.listen, 119
Stt.results <string_list_ptr_nvar>, 120
Su.close, 167
Su.open, 167
Su.read.line <svar>, 167
Su.read.ready <nvar>, 167
Su.write <sexp>, 167
Sw.begin <nexp>|<sexp>, 76
Sw.break, 76
Sw.case <nexp >|<sexp>, 76
Sw.default, 77
Sw.end, 77
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 124
System.close, 166
System.open, 166
System.read.line <svar>, 166
System.read.ready <nvar>, 166
System.write <sexp>, 166
TAN(<nexp>), 55
Text.close <file_table_nexp>, 94
Text.eof <file_table_nexp>, <lvar>, 95
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 89
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 94
Text.position.get <file_table_nexp>, <position_nvar>, 95
Text.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 96
Text.position.set <file_table_nexp>, <position_nexp>, 96
Text.readln <file_table_nexp>, <line_svar>, 94
Text.writeln <file_table_nexp>, {<exp> {,|;}} ..., 95
TGet <result_svar>, <prompt_sexp> {, <title_sexp>}, 89
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 114
TIME(), 58
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 58
Timer.clear, 115
Timer.resume, 115
Timer.set <interval_nexp>, 115
TimeZone.get <tz_svar>, 114
TimeZone.list <tz_list_pointer_nexp>, 114
TimeZone.set { <tz_sexp> }, 114
TODEGREES(<nexp>), 55
Tone <frequency_nexp>, <duration_nexp>{, <duration_chk_lexp}, 124
TORADIANS(<nexp>), 55
Tts.init, 119
Tts.speak <sexp> {, <wait_lexp>}, 119
Tts.speak.toFile <sexp> {, <path_sexp>}, 119
Tts.stop, 119
UCODE(<sexp>{, <index_nexp>}), 56
UnDim Array[], Array$[], ..., 37
UPPER$(<sexp>), 60
USING$({<locale_sexp>} , <format_sexp> { , <exp>}...), 61
VAL(<sexp>), 55
VERSION$(), 60
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 124
W_R.break, 72
W_R.continue, 72
WakeLock <code_nexp>, 125
While <lexp> / Repeat, 71
WiFi.info {{<SSID_svar>}{, <BSSID_svar>}{, <MAC_svar>}{, <IP_var>}{, <speed_nvar>}}, 126
WifiLock <code_nexp>, 125
WORD$(<source_sexp>, <n_nexp> {, <test_sexp>}), 60
