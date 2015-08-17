 There is 200+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 50
!! - Block Comment, 50
# - Format Line, 26
% - Middle of Line Comment, 50
? {<exp> {,|;}} ..., 89
ABS(<nexp>), 54
ACOS(<nexp>), 57
Array.average <Average_nvar>, Array[{<start>,<length>}], 39
Array.copy SourceArray$[{<start>,<length>}], DestinationArray$[{{-}<extras>}], 39
Array.copy SourceArray[{<start>,<length>}], DestinationArray[{{-}<extras>}], 39
Array.delete Array[], Array$[] ..., 39
Array.length n, Array$[{<start>,<length>}], 40
Array.length n, Array[{<start>,<length>}], 40
Array.load Array$[], <sexp>, ..., 40
Array.load Array[], <nexp>, ..., 40
Array.max <max_nvar> Array[{<start>,<length>}], 40
Array.min <min_nvar>, Array[{<start>,<length>}], 40
Array.reverse Array$[{<start>,<length>}], 40
Array.reverse Array[{<start>,<length>}], 40
Array.search Array$[{<start>,<length>}], <value_sexp>, <result_nvar>{,<start_nexp>}, 40
Array.search Array[{<start>,<length>}], <value_nexp>, <result_nvar>{,<start_nexp>}, 40
Array.shuffle Array[{<start>,<length>}], 41
Array.sort Array[{<start>,<length>}], 41
Array.std_dev <sd_nvar>, Array[{<start>,<length>}], 41
Array.sum <sum_nvar>, Array[{<start>,<length>}], 41
Array.variance <v_nvar>, Array[{<start>,<length>}], 41
ASCII(<sexp>{, <index_nexp>}), 58
ASIN(<nexp>), 57
ATAN(<nexp>), 57
ATAN2 (<nexp_y>, <nexp_x>), 57
Audio.isdone <lvar>, 164
Audio.length <length_nvar>, <aft_nexp>, 164
Audio.load <aft_nvar>, <filename_sexp>, 163
Audio.loop, 163
Audio.pause, 163
Audio.play <aft_nexp>, 163
Audio.position.current <nvar>, 164
Audio.position.seek <nexp>, 164
Audio.record.start <fn_svar>, 164
Audio.record.stop, 164
Audio.release <aft_nexp>, 164
Audio.stop, 163
Audio.volume <left_nexp>, <right_nexp>, 163
Back.resume, 83
BACKGROUND(), 60
Background.resume, 133
BAND(<nexp1>, <nexp2>), 54
BIN$(<nexp>), 66
BIN(<sexp>), 58
BNOT(<nexp>, 54
BOR(<nexp1>, <nexp2>), 53
Browse <url_sexp>, 108
Bt.close, 116
Bt.connect {0|1}, 116
Bt.device.name <svar>, 118
Bt.disconnect, 116
Bt.onReadReady.resume, 117
Bt.open {0|1}, 115
Bt.read.bytes <svar>, 118
Bt.read.ready <nvar>, 117
Bt.reconnect, 116
Bt.set.UUID <sexp>, 118
Bt.status {{<connect_var>}{, <name_svar>}{, <address_svar>}}, 116
Bt.write {<exp> {,|;}} ..., 117
Bundle.clear <pointer_nexp>, 48
Bundle.contain <pointer_nexp>, <key_sexp> , <contains_nvar>, 47
Bundle.create <pointer_nvar>, 46
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 46
Bundle.keys <bundle_ptr_nexp>, <list_ptr_nexp>, 47
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 46
Bundle.remove <pointer_nexp>, <key_sexp>, 48
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 47
BXOR(<nexp1>, <nexp2>), 54
Byte.close <file_table_nexp>, 103
Byte.copy <file_table_nexp>,<output_file_sexp>, 105
Byte.eof <file_table_nexp>, <lvar>, 104
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 103
Byte.position.get <file_table_nexp>, <position_nexp>, 104
Byte.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 105
Byte.position.set <file_table_nexp>, <position_nexp>, 105
Byte.read.buffer <file_table_nexp>, <count_nexp>, <buffer_svar>, 104
Byte.read.byte <file_table_nexp>, <byte_nvar>, 103
Byte.truncate <file_table_nexp>,<length_nexp>, 105
Byte.write.buffer <file_table_nexp>, <sexp>, 104
Byte.write.byte <file_table_nexp>, <byte_nexp>|<sexp>, 104
Call <user_defined_function>, 74
CBRT(<nexp>), 56
CEIL(<nexp>), 55
CHR$ (<nexp>, ...), 61
Clipboard.get <svar>, 122
Clipboard.put <sexp>, 122
CLOCK(), 60
Cls, 89
Console.line.count <count_nvar >, 90
Console.line.text <line_nexp>, <text_svar>, 90
Console.line.touched <line_nvar> {, <press_lvar>}, 90
Console.save <filename_sexp>, 90
Console.title { <title_sexp>}, 88
ConsoleTouch.resume, 83
COS(<nexp>), 57
COSH(<nexp>), 57
D_U.break, 78
D_U.continue, 78
Debug.dump.array Array[], 86
Debug.dump.bundle <bundlePtr_nexp>, 86
Debug.dump.list <listPtr_nexp>, 86
Debug.dump.scalars, 85
Debug.dump.stack <stackPtr_nexp>, 86
Debug.echo.off, 85
Debug.echo.on, 85
Debug.off, 85
Debug.on, 85
Debug.print, 85
Debug.show, 87
Debug.show.array Array[], 86
Debug.show.bundle <bundlePtr_nexp>, 86
Debug.show.list <listPtr_nexp>, 86
Debug.show.program, 87
Debug.show.scalars, 86
Debug.show.stack <stackPtr_nexp>, 87
Debug.show.watch, 87
Debug.watch var, ..., 87
DECODE$(<charset_sexp>, <buffer_sexp>), 65
DECODE$(<type_sexp>, {<qualifier_sexp>}, <source_sexp>), 64
Decrypt <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 123
Device <nvar>|<nexp>, 128
Device <svar>, 128
Dialog.message {<title_sexp>}, {<message_sexp>}, <selection_nvar> {, <button1_sexp>{, <button2_sexp>{, <button3_sexp>}}}, 91
Dialog.select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp>}, 91
Dim Array [n, n, ...], Array$[n, n, ...] ..., 38
Do / Until <lexp>, 77
Echo.off, 122
Echo.on, 122
Email.send <recipient_sexp>, <subject_sexp>, <body_sexp>, 118
ENCODE$(<charset_sexp>, <source_sexp>), 65
ENCODE$(<type_sexp>, {<qualifier_sexp>}, <source_sexp>), 63
Encrypt {<pw_sexp>}, <source_sexp>, <encrypted_svar>, 122
End{ <msg_sexp>}, 84
ENDS_WITH(<sub_sexp>, <base_sexp>), 59
Exit, 84
EXP(<nexp>), 56
F_N.break, 77
F_N.continue, 77
File.delete <lvar>, <path_sexp>, 98
File.dir <path_sexp>, Array$[] {, <dirmark_sexp>}, 98
File.exists <lvar>, <path_sexp>, 98
File.mkdir <path_sexp>, 99
File.rename <old_path_sexp>, <new_path_sexp>, 99
File.root <svar>, 99
File.size <size_nvar>, <path_sexp>, 99
File.type <type_svar>, <path_sexp>, 99
FLOOR(<nexp>), 55
Fn.def name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 73
Fn.end, 74
Fn.rtn <sexp>|<nexp>, 74
Font.clear, 88
Font.delete {<font_ptr_nexp>}, 88
Font.load <font_ptr_nvar>, <filename_sexp>, 88
For - To - Step / Next, 76
FORMAT$(<pattern_sexp>, <nexp>), 70
FORMAT_USING$(<locale_sexp>, <format_sexp> { , <exp>}...), 70
FRAC(<nexp>), 55
Ftp.cd <new_directory_sexp>, 114
Ftp.close, 113
Ftp.delete <filename_sexp>, 114
Ftp.dir <list_nvar>, 114
Ftp.get <source_sexp>, <destination_sexp>, 114
Ftp.mkdir <directory_sexp>, 115
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 113
Ftp.put <source_sexp>, <destination_sexp>, 113
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 114
Ftp.rmdir <directory_sexp>, 114
GETERROR$(), 61
GoSub <index_nexp>, <label>... / Return, 80
GoSub <label> / Return, 79
GoTo <index_nexp>, <label>..., 79
GoTo <label>, 79
Gps.accuracy <nvar>, 171
Gps.altitude <nvar>, 171
Gps.bearing <nvar>, 171
Gps.close, 168
Gps.latitude <nvar>, 171
Gps.location {{<time_nvar>}, {<prov_svar>}, {<count_nvar}, {<acc_nvar>}, {<lat_nvar>}, {<long_nvar>}, {<alt_nvar>}, {<bear_nvar>}, {<speed_nvar>}}, 170
Gps.longitude <nvar>, 171
Gps.open {{<status_nvar>},{<time_nexp>},{<distance_nexp>}}, 167
Gps.provider <svar>, 171
Gps.satellites {{<count_nvar>}, {<sat_list_nexp>}}, 171
Gps.speed <nvar>, 171
Gps.status {{<status_var>}, {<infix_nvar>},{inview_nvar}, {<sat_list_nexp>}}, 168
Gps.time <nvar>, 171
Gr.arc <object_number_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 144
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 152
Gr.bitmap.crop <new_bitmap_ptr_nvar>, <source_bitmap_ptr_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 153
Gr.bitmap.delete <bitmap_ptr_nexp>, 153
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nexp>, x , y, 154
Gr.bitmap.drawinto.end, 155
Gr.bitmap.drawinto.start <bitmap_ptr_nexp>, 154
Gr.bitmap.fill <bitmap_ptr_nexp>, <x_nexp>, <y_nexp>, 154
Gr.bitmap.load <bitmap_ptr_nvar>, <file_name_sexp>, 152
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 154
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nexp>, width, height {, <smoothing_lexp> }, 153
Gr.bitmap.size <bitmap_ptr_nexp>, width, height, 153
Gr.bounded.touch touched, left, top, right, bottom, 148
Gr.bounded.touch2 touched, left, top, right, bottom, 148
Gr.brightness <nexp>, 142
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 156
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 157
Gr.camera.select 1|2, 156
Gr.camera.shoot <bm_ptr_nvar>, 156
Gr.circle <object_number_nvar>, x, y, radius, 144
Gr.clip <object__ptr_nexp>, <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 161
Gr.close, 142
Gr.cls, 142
Gr.color {{alpha}{, red}{, green}{, blue}{, style}}, 139
Gr.front flag, 142
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 154
Gr.get.params <object_ptr_nexp>, <param_array$[]>, 158
Gr.get.pixel x, y, alpha, red, green, blue, 157
Gr.get.position <object_ptr_nexp>, x, y, 158
Gr.get.textbounds <sexp>, left, top, right, bottom, 149
Gr.get.type <object_ptr_nexp>, <type_svar>, 158
Gr.get.value <object_ptr_nexp> {, <tag_sexp>, <value_nvar | value_svar>}..., 158
Gr.getDL <dl_array[]> {, <keep_all_objects_lexp> }, 162
Gr.group <object_number_nvar>{, <obj_nexp>}..., 146
Gr.group.getDL <object_number_nvar>, 146
Gr.group.list <object_number_nvar>, <list_ptr_nexp>, 146
Gr.group.newDL <object_number_nvar>, 146
Gr.hide <object_number_nexp>, 146
Gr.line <object_number_nvar>, x1, y1, x2, y2, 143
Gr.modify <object_ptr_nexp> {, <tag_sexp>, <value_nexp | value_sexp>}..., 158
Gr.move <object_ptr_nexp>{{, dx}{, dy}}, 158
Gr.newDL <dl_array[{<start>,<length>}]>, 162
Gr.onGrTouch.resume, 148
Gr.open {{alpha}{, red}{, green}{, blue}{, <ShowStatusBar_lexp>}{, <Orientation_nexp>}}, 139
Gr.orientation <nexp>, 140
Gr.oval <object_number_nvar>, left, top, right, bottom, 143
Gr.paint.get <object_ptr_nvar>, 160
Gr.point <object_number_nvar>, x, y, 143
Gr.poly <object_number_nvar>, list_pointer {,x,y}, 145
Gr.rect <object_number_nvar>, left, top, right, bottom, 143
Gr.render, 141
Gr.rotate.end {<obj_nvar>}, 155
Gr.rotate.start angle, x, y{,<obj_nvar>}, 155
Gr.save <filename_sexp> {,<quality_nexp>}, 157
Gr.scale x_factor, y_factor, 141
Gr.screen width, height{, density }, 141
Gr.screen.to_bitmap <bm_ptr_nvar>, 157
Gr.set.antialias <lexp>, 140
Gr.set.pixels <object_number_nvar>, pixels[{<start>,<length>}] {,x,y}, 144
Gr.set.stroke <nexp>, 140
Gr.show <object_number_nexp>, 147
Gr.show.toggle <object_number_nexp>, 147
Gr.statusbar {<height_nvar>} {, showing_lvar}, 141
Gr.statusbar.show <nexp>, 141
Gr.text.align type, 148
Gr.text.bold <lexp>, 151
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 151
Gr.text.height {<height_nvar>} {, <up_nvar>} {, <down_nvar>}, 148
Gr.text.setfont {<font_ptr_nexp>|<font_family_sexp>} {, <style_sexp>}, 150
Gr.text.size <nexp>, 148
Gr.text.skew <nexp>, 151
Gr.text.strike <lexp>, 151
Gr.text.typeface {<nexp>} {, <style_nexp>}, 150
Gr.text.underline <lexp>, 151
Gr.text.width <nvar>, <sexp>, 149
Gr.touch touched, x, y, 147
Gr.touch2 touched, x, y, 148
GR_COLLISION ( <object_1_nvar>, <object_2_nvar>), 59
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 102
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 102
Headset <state_nvar>, <type_svar>, <mic_nvar>, 129
HEX$(<nexp>), 66
HEX(<sexp>), 58
Home, 133
Html.clear.cache, 108
Html.clear.history, 108
Html.close, 108
Html.get.datalink <data_svar>, 107
Html.go.back, 108
Html.go.forward, 108
Html.load.string <html_sexp>, 107
Html.load.url <file_sexp>, 106
Html.open {<ShowStatusBar_lexp> {, <Orientation_nexp>}}, 106
Html.orientation <nexp>, 106
Html.post <url_sexp>, <list_nexp>, 107
Http.post <url_sexp>, <list_nexp>, <result_svar>, 109
HYPOT(<nexp_x>, <nexp_y), 57
If / Then / Else, 75
If / Then / Else / Elseif / Endif, 75
Include FilePath, 80
Inkey$ <svar>, 92
Input {<prompt_sexp>}, <result_var>{, {<default_exp>}{,<canceled_nvar>}}, 92
INT$(<nexp>), 66
INT(<nexp>), 55
IS_IN(<sub_sexp>, <base_sexp>{, <start_nexp>}, 59
Join <source_array$[]>, <result_svar> {, <separator_sexp>{, <wrapper_sexp}}, 124
Join.all <source_array$[]>, <result_svar> {, <separator_sexp>{, <wrapper_sexp}}, 124
Kb.hide, 94
Kb.resume, 95
Kb.show, 95
Kb.showing <lvar>, 95
Kb.toggle, 95
Key.resume, 84
LEFT$ (<sexp>, <count_nexp>), 61
LEN(<sexp>), 58
Let, 52
List.add <pointer_nexp>{, <exp>}..., 43
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 44
List.add.array string_list_pointer, Array$[{<start>,<length>}], 44
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 43
List.clear <pointer_nexp>, 45
List.create N|S, <pointer_nvar>, 43
List.get <pointer_nexp>,<index_nexp>, <svar>|<nvar>, 44
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 44
List.remove <pointer_nexp>,<index_nexp>, 44
List.replace <pointer_nexp>,<index_nexp>, <sexp>|<nexp>, 44
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 45
List.size <pointer_nexp>, <nvar>, 45
List.toArray <pointer_nexp>, Array$[] | Array[], 45
List.type <pointer_nexp>, <svar>, 44
LOG(<nexp>), 56
LOG10(<nexp>), 56
LOWER$(<sexp>), 65
MAX(<nexp>, <nexp>), 55
MenuKey.resume, 83
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 61
MIN(<nexp>, <nexp>), 55
MOD(<nexp1>, <nexp2>), 55
MyPhoneNumber <svar>, 118
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 129
OCT$(<nexp>, 66
OCT(<sexp>), 58
OnBackGround:, 133
OnBackKey:, 83
OnBtReadReady:, 117
OnConsoleTouch:, 83
OnError:, 82
OnGrTouch:, 148
OnKbChange:, 95
OnKeyPress:, 83
OnMenuKey:, 83
OnTimer:, 121
Pause <ticks_nexp>, 130
Phone.call <sexp>, 118
Phone.dial <sexp>, 119
Phone.info <nvar>|<nexp>, 129
Phone.rcv.init, 119
Phone.rcv.next <state_nvar>, <number_svar>, 119
PI(), 57
Popup <message_sexp>{{, <x_nexp>}{, <y_nexp>}{, <duration_lexp>}}, 93
POW(<nexp1>, <nexp2>), 57
Print {<exp> {,|;}} ..., 89
RANDOMIZE(<nexp>), 54
Read.data <number>|<string> {,<number>|<string>...,<number>|<string>}, 84
Read.from <nexp>, 85
Read.next <var>, ..., 84
Rem, 50
REPLACE$( <sexp>, <argument_sexp>, <replace_sexp>), 62
RIGHT$(<sexp>, <count_nexp>), 62
Ringer.get.mode <nvar>, 123
Ringer.get.volume <vol_nvar> { , <max_nvar>, 123
Ringer.set.mode <nexp>, 123
Ringer.set.volume <nexp>, 123
RND(), 55
ROUND(<value_nexp>{, <count_nexp>{, <mode_sexp>}}), 55
Run <filename_sexp> {, <data_sexp>}, 81
Select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 93
Sensors.close, 173
Sensors.list <sensor_array$[]>, 172
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 173
Sensors.read sensor_type, p1, p2, p3, 173
SGN(<nexp>), 54
SHIFT (<value_nexp>, <bits_nexp>), 58
SIN(<nexp>), 57
SINH(<nexp>), 57
Sms.rcv.init, 119
Sms.rcv.next <svar>, 119
Sms.send <number_sexp>, <message_sexp>, 119
Socket.client.close, 111
Socket.client.connect <server_sexp>, <port_nexp> { , <wait_lexp> }, 110
Socket.client.read.file <file_nexp>, 111
Socket.client.read.line <line_svar>, 110
Socket.client.read.ready <nvar>, 110
Socket.client.server.ip <svar>, 110
Socket.client.status <status_nvar>, 110
Socket.client.write.bytes <sexp>, 111
Socket.client.write.file <file_nexp>, 111
Socket.client.write.line <line_sexp>, 111
Socket.myIP <svar>, 111
Socket.server.client.ip <nvar>, 113
Socket.server.close, 113
Socket.server.connect {<wait_lexp>}, 112
Socket.server.create <port_nexp>, 111
Socket.server.disconnect, 113
Socket.server.read.file <file_nexp>, 113
Socket.server.read.line <svar>, 112
Socket.server.read.ready <nvar>, 112
Socket.server.status <status_nvar>, 112
Socket.server.write.bytes <sexp>, 112
Socket.server.write.file <file_nexp>, 113
Socket.server.write.line <line_sexp>, 112
Soundpool.load <soundID_nvar>, <file_path_sexp>, 165
Soundpool.open <MaxStreams_nexp>, 165
Soundpool.pause <streamID_nexp>, 166
Soundpool.play <streamID_nvar>, <soundID_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 165
Soundpool.release, 166
Soundpool.resume <streamID_nexp>, 166
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 166
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 166
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 166
Soundpool.stop <streamID_nexp>, 166
Soundpool.unload <soundID_nexp>, 165
Split <result_array$[]>, <sexp> {, <test_sexp>}, 124
Split.all <result_array$[]>, <sexp> {, <test_sexp>}, 124
Sql.close <DB_pointer_nvar>, 134
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 136
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 134
Sql.exec <DB_pointer_nvar>, <command_sexp>, 136
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$, ...,CN$, VN$, 134
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 134
Sql.next <done_lvar>, <cursor_nvar>, C1V$, C2V$, .., CNV$, 136
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 134
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp> {, <order_sexp>} } }, 135
Sql.query.length <length_nvar>, <cursor_nvar>, 135
Sql.query.position <position_nvar>, <cursor_nvar>, 135
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 136
Sql.update <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN${: <where_sexp>, 136
SQR(<nexp>), 56
Stack.clear <ptr_nexp>, 49
Stack.create N|S, <ptr_nvar>, 48
Stack.isEmpty <ptr_nexp>, <nvar>, 49
Stack.peek <ptr_nexp>, <nvar>|<svar>, 49
Stack.pop <ptr_nexp>, <nvar>|<svar>, 48
Stack.push <ptr_nexp>, <nexp>|<sexp>, 48
Stack.type <ptr_nexp>, <svar>, 49
STARTS_WITH (<sub_sexp>, <base_sexp>{, <start_nexp>}, 59
STR$(<nexp>), 65
STT.listen, 126
STT.results <string_list_ptr_nexp>, 126
Su.close, 174
Su.open, 174
Su.read.line <svar>, 174
Su.read.ready <nvar>, 174
Su.write <sexp>, 174
Sw.begin <nexp>|<sexp>, 82
Sw.break, 82
Sw.case <nexp >|<sexp>, 82
Sw.default, 82
Sw.end, 82
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 131
System.close, 174
System.open, 173
System.read.line <svar>, 174
System.read.ready <nvar>, 173
System.write <sexp>, 173
TAN(<nexp>), 57
Text.close <file_table_nexp>, 100
Text.eof <file_table_nexp>, <lvar>, 101
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 94
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 100
Text.position.get <file_table_nexp>, <position_nvar>, 101
Text.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 102
Text.position.set <file_table_nexp>, <position_nexp>, 102
Text.readln <file_table_nexp>, <line_svar>, 100
Text.writeln <file_table_nexp>, {<exp> {,|;}} ..., 101
TGet <result_svar>, <prompt_sexp> {, <title_sexp>}, 94
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 120
TIME(), 60
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 60
Timer.clear, 121
Timer.resume, 121
Timer.set <interval_nexp>, 121
TimeZone.get <tz_svar>, 121
TimeZone.list <tz_list_pointer_nexp>, 121
TimeZone.set { <tz_sexp> }, 121
TODEGREES(<nexp>), 58
Tone <frequency_nexp>, <duration_nexp>{, <duration_chk_lexp}, 131
TORADIANS(<nexp>), 58
TRIM$(<sexp>{, <test_sexp>}), 62
TTS.init, 125
TTS.speak <sexp> {, <wait_lexp>}, 125
TTS.speak.toFile <sexp> {, <path_sexp>}, 125
TTS.stop, 126
UCODE(<sexp>{, <index_nexp>}), 58
UnDim Array[], Array$[], ..., 39
UPPER$(<sexp>), 66
USING$({<locale_sexp>} , <format_sexp> { , <exp>}...), 66
VAL(<sexp>), 58
VERSION$(), 66
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 131
W_R.break, 77
W_R.continue, 77
WakeLock <code_nexp>, 131
While <lexp> / Repeat, 77
WiFi.info {{<SSID_svar>}{, <BSSID_svar>}{, <MAC_svar>}{, <IP_var>}{, <speed_nvar>}}, 132
WifiLock <code_nexp>, 132
WORD$(<source_sexp>, <n_nexp> {, <test_sexp>}), 62
