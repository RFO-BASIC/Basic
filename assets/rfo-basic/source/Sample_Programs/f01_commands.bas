 There is 200+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 50
!! - Block Comment, 50
# - Format Line, 26
% - Middle of Line Comment, 50
? {<exp> {,|;}} ..., 90
ABS(<nexp>), 55
ACOS(<nexp>), 58
App.broadcast <action_sexp>, <data_uri_sexp>, <package_sexp>, <component_sexp>, <mime_type_sexp>, <categories_sexp>, <extras_bptr_nexp>, <flags_nexp>, 184
App.start <action_sexp>, <data_uri_sexp>, <package_sexp>, <component_sexp>, <mime_type_sexp>, <categories_sexp>, <extras_bptr_nexp>, <flags_nexp>, 185
Array.average <Average_nvar>, Array[{<start>,<length>}], 39
Array.copy SourceArray$[{<start>,<length>}], DestinationArray$[{{-}<extras>}], 39
Array.copy SourceArray[{<start>,<length>}], DestinationArray[{{-}<extras>}], 39
Array.delete Array[], Array$[] ..., 40
Array.fill Array$[{<start>,<length>}], <sexp>, 40
Array.fill Array[{<start>,<length>}], <nexp>, 40
Array.length n, Array$[{<start>,<length>}], 40
Array.length n, Array[{<start>,<length>}], 40
Array.load Array$[], <sexp>, ..., 40
Array.load Array[], <nexp>, ..., 40
Array.max <max_nvar> Array[{<start>,<length>}], 41
Array.min <min_nvar>, Array[{<start>,<length>}], 41
Array.reverse Array$[{<start>,<length>}], 41
Array.reverse Array[{<start>,<length>}], 41
Array.search Array$[{<start>,<length>}], <value_sexp>, <result_nvar>{,<start_nexp>}, 41
Array.search Array[{<start>,<length>}], <value_nexp>, <result_nvar>{,<start_nexp>}, 41
Array.shuffle Array[{<start>,<length>}], 41
Array.sort Array[{<start>,<length>}], 41
Array.std_dev <sd_nvar>, Array[{<start>,<length>}], 41
Array.sum <sum_nvar>, Array[{<start>,<length>}], 41
Array.variance <v_nvar>, Array[{<start>,<length>}], 41
ASCII(<sexp>{, <index_nexp>}), 59
ASIN(<nexp>), 58
ATAN(<nexp>), 58
ATAN2(<nexp_y>, <nexp_x>), 58
Audio.isdone <lvar>, 174
Audio.length <length_nvar>, <aft_nexp>, 173
Audio.load <aft_nvar>, <filename_sexp>, 172
Audio.loop, 173
Audio.pause, 173
Audio.play <aft_nexp>, 172
Audio.position.current <nvar>, 173
Audio.position.seek <nexp>, 173
Audio.record.start <fn_svar>, 174
Audio.record.stop, 174
Audio.release <aft_nexp>, 174
Audio.stop, 173
Audio.volume <left_nexp>, <right_nexp>, 173
Back.resume, 84
BACKGROUND(), 60
Background.resume, 137
BAND(<nexp1>, <nexp2>), 54
BIN$(<nexp>), 67
BIN(<sexp>), 59
BNOT(<nexp>, 55
BOR(<nexp1>, <nexp2>), 54
Browse <url_sexp>, 111
Bt.close, 118
Bt.connect {0|1}, 119
Bt.device.name <svar>, 120
Bt.disconnect, 119
Bt.onReadReady.resume, 120
Bt.open {0|1}, 118
Bt.read.bytes <svar>, 120
Bt.read.ready <nvar>, 120
Bt.reconnect, 119
Bt.set.UUID <sexp>, 121
Bt.status {{<connect_var>}{, <name_svar>}{, <address_svar>}}, 119
Bt.write {<exp> {,|;}} ..., 120
Bundle.clear <pointer_nexp>, 48
Bundle.contain <pointer_nexp>, <key_sexp> , <contains_nvar>, 48
Bundle.create <pointer_nvar>, 47
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 47
Bundle.keys <bundle_ptr_nexp>, <list_ptr_nexp>, 47
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 47
Bundle.remove <pointer_nexp>, <key_sexp>, 48
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 48
BXOR(<nexp1>, <nexp2>), 54
Byte.close <file_table_nexp>, 105
Byte.copy <file_table_nexp>,<output_file_sexp>, 108
Byte.eof <file_table_nexp>, <lvar>, 107
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 104
Byte.position.get <file_table_nexp>, <position_nexp>, 107
Byte.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 107
Byte.position.set <file_table_nexp>, <position_nexp>, 107
Byte.read.buffer <file_table_nexp>, <count_nexp>, <buffer_svar>, 106
Byte.read.byte <file_table_nexp> {,<nvar>}..., 105
Byte.read.number <file_table_nexp> {,<nvar>...}, 106
Byte.truncate <file_table_nexp>,<length_nexp>, 108
Byte.write.buffer <file_table_nexp>, <sexp>, 107
Byte.write.byte <file_table_nexp> {{,<nexp>}...{,<sexp>}}, 105
Byte.write.number <file_table_nexp> {,<nexp>}..., 106
Call <user_defined_function>, 75
CBRT(<nexp>), 57
CEIL(<nexp>), 56
CHR$(<nexp>, ...), 62
Clipboard.get <svar>, 125
Clipboard.put <sexp>, 125
CLOCK(), 61
Cls, 91
Console.front, 91
Console.line.count <count_nvar >, 91
Console.line.text <line_nexp>, <text_svar>, 91
Console.line.touched <line_nvar> {, <press_lvar>}, 91
Console.save <filename_sexp>, 91
Console.title { <title_sexp>}, 92
ConsoleTouch.resume, 84
COS(<nexp>), 58
COSH(<nexp>), 58
D_U.break, 79
D_U.continue, 79
Debug.dump.array Array[], 87
Debug.dump.bundle <bundlePtr_nexp>, 87
Debug.dump.list <listPtr_nexp>, 87
Debug.dump.scalars, 87
Debug.dump.stack <stackPtr_nexp>, 87
Debug.echo.off, 87
Debug.echo.on, 86
Debug.off, 86
Debug.on, 86
Debug.print, 87
Debug.show, 89
Debug.show.array Array[], 87
Debug.show.bundle <bundlePtr_nexp>, 88
Debug.show.list <listPtr_nexp>, 88
Debug.show.program, 88
Debug.show.scalars, 87
Debug.show.stack <stackPtr_nexp>, 88
Debug.show.watch, 88
Debug.watch var, ..., 88
DECODE$(<charset_sexp>, <buffer_sexp>), 66
DECODE$(<type_sexp>, {<qualifier_sexp>}, <source_sexp>), 64
Decrypt <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 125
Device <nvar>|<nexp>, 131
Device <svar>, 131
Dialog.message {<title_sexp>}, {<message_sexp>}, <selection_nvar> {, <button1_sexp>{, <button2_sexp>{, <button3_sexp>}}}, 92
Dialog.select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp>}, 93
Dim Array [n, n, ...], Array$[n, n, ...] ..., 39
Dir <path_sexp>, Array$[] {, <dirmark_sexp>}, 100
Do / Until <lexp>, 78
Echo.off, 125
Echo.on, 125
Email.send <recipient_sexp>, <subject_sexp>, <body_sexp>, 121
ENCODE$(<charset_sexp>, <source_sexp>), 65
ENCODE$(<type_sexp>, {<qualifier_sexp>}, <source_sexp>), 64
Encrypt {<pw_sexp>}, <source_sexp>, <encrypted_svar>, 125
End{ <msg_sexp>}, 85
ENDS_WITH(<sub_sexp>, <base_sexp>), 60
Exit, 85
EXP(<nexp>), 57
F_N.break, 78
F_N.continue, 78
File.delete <lvar>, <path_sexp>, 100
File.dir <path_sexp>, Array$[] {, <dirmark_sexp>}, 100
File.exists <lvar>, <path_sexp>, 100
File.mkdir <path_sexp>, 100
File.rename <old_path_sexp>, <new_path_sexp>, 100
File.root <svar>, 101
File.size <size_nvar>, <path_sexp>, 101
File.type <type_svar>, <path_sexp>, 101
FLOOR(<nexp>), 56
Fn.def name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 74
Fn.end, 75
Fn.rtn <sexp>|<nexp>, 75
Font.clear, 89
Font.delete {<font_ptr_nexp>}, 89
Font.load <font_ptr_nvar>, <filename_sexp>, 89
For - To - Step / Next, 77
FORMAT$(<pattern_sexp>, <nexp>), 71
FORMAT_USING$(<locale_sexp>, <format_sexp> { , <exp>}...), 71
FRAC(<nexp>), 56
Ftp.cd <new_directory_sexp>, 117
Ftp.close, 116
Ftp.delete <filename_sexp>, 117
Ftp.dir <list_nvar>, 117
Ftp.get <source_sexp>, <destination_sexp>, 116
Ftp.mkdir <directory_sexp>, 117
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 116
Ftp.put <source_sexp>, <destination_sexp>, 116
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 117
Ftp.rmdir <directory_sexp>, 117
GETERROR$(), 61
GoSub <index_nexp>, <label>... / Return, 81
GoSub <label> / Return, 80
GoTo <index_nexp>, <label>..., 80
GoTo <label>, 80
Gps.accuracy <nvar>, 181
Gps.altitude <nvar>, 181
Gps.bearing <nvar>, 181
Gps.close, 177
Gps.latitude <nvar>, 181
Gps.location {{<time_nvar>}, {<prov_svar>}, {<count_nvar}, {<acc_nvar>}, {<lat_nvar>}, {<long_nvar>}, {<alt_nvar>}, {<bear_nvar>}, {<speed_nvar>}}, 180
Gps.longitude <nvar>, 181
Gps.open {{<status_nvar>},{<time_nexp>},{<distance_nexp>}}, 176
Gps.provider <svar>, 180
Gps.satellites {{<count_nvar>}, {<sat_list_nexp>}}, 180
Gps.speed <nvar>, 181
Gps.status {{<status_var>}, {<infix_nvar>},{inview_nvar}, {<sat_list_nexp>}}, 177
Gps.time <nvar>, 180
Gr.arc <object_number_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 150
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 160
Gr.bitmap.crop <new_bitmap_ptr_nvar>, <source_bitmap_ptr_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 162
Gr.bitmap.delete <bitmap_ptr_nexp>, 161
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nexp>, x , y, 162
Gr.bitmap.drawinto.end, 163
Gr.bitmap.drawinto.start <bitmap_ptr_nexp>, 163
Gr.bitmap.fill <bitmap_ptr_nexp>, <x_nexp>, <y_nexp>, 163
Gr.bitmap.load <bitmap_ptr_nvar>, <file_name_sexp>, 161
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 162
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nexp>, width, height {, <smoothing_lexp> }, 161
Gr.bitmap.size <bitmap_ptr_nexp>, width, height, 161
Gr.bounded.touch touched, left, top, right, bottom, 154
Gr.bounded.touch2 touched, left, top, right, bottom, 155
Gr.brightness <nexp>, 149
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 166
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 167
Gr.camera.select 1|2, 165
Gr.camera.shoot <bm_ptr_nvar>, 166
Gr.circle <object_number_nvar>, x, y, radius, 150
Gr.clip <object__ptr_nexp>, <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 170
Gr.close, 148
Gr.cls, 148
Gr.color {{alpha}{, red}{, green}{, blue}{, style}{, paint}}, 145
Gr.front flag, 148
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 162
Gr.get.params <object_ptr_nexp>, <param_array$[]>, 167
Gr.get.pixel x, y, alpha, red, green, blue, 167
Gr.get.position <object_ptr_nexp>, x, y, 168
Gr.get.textbounds <exp>, left, top, right, bottom, 159
Gr.get.type <object_ptr_nexp>, <type_svar>, 167
Gr.get.value <object_ptr_nexp> {, <tag_sexp>, <value_nvar | value_svar>}..., 168
Gr.getDL <dl_array[]> {, <keep_all_objects_lexp> }, 171
Gr.group <object_number_nvar>{, <obj_nexp>}..., 152
Gr.group.getDL <object_number_nvar>, 153
Gr.group.list <object_number_nvar>, <list_ptr_nexp>, 153
Gr.group.newDL <object_number_nvar>, 153
Gr.hide <object_number_nexp>, 153
Gr.line <object_number_nvar>, x1, y1, x2, y2, 149
Gr.modify <object_ptr_nexp> {, <tag_sexp>, <value_nexp | value_sexp>}..., 168
Gr.move <object_ptr_nexp>{{, dx}{, dy}}, 168
Gr.newDL <dl_array[{<start>,<length>}]>, 171
Gr.onGrTouch.resume, 155
Gr.open {{alpha}{, red}{, green}{, blue}{, <ShowStatusBar_lexp>}{, <Orientation_nexp>}}, 144
Gr.orientation <nexp>, 146
Gr.oval <object_number_nvar>, left, top, right, bottom, 150
Gr.paint.copy {{<src_nexp>}{, <dst_nexp>}}, 163
Gr.paint.get <object_ptr_nvar>, 163
Gr.paint.reset {<nexp>}, 164
Gr.point <object_number_nvar>, x, y, 149
Gr.poly <object_number_nvar>, list_pointer {,x,y}, 151
Gr.rect <object_number_nvar>, left, top, right, bottom, 149
Gr.render, 147
Gr.rotate.end {<obj_nvar>}, 165
Gr.rotate.start angle, x, y{,<obj_nvar>}, 164
Gr.save <filename_sexp> {,<quality_nexp>}, 167
Gr.scale x_factor, y_factor, 148
Gr.screen width, height{, density }, 147
Gr.screen.to_bitmap <bm_ptr_nvar>, 167
Gr.set.antialias {{<lexp>}{,<paint_nexp>}}, 146
Gr.set.pixels <object_number_nvar>, pixels[{<start>,<length>}] {,x,y}, 150
Gr.set.stroke {{<nexp>}{,<paint_nexp>}}, 146
Gr.show <object_number_nexp>, 153
Gr.show.toggle <object_number_nexp>, 153
Gr.statusbar {<height_nvar>} {, showing_lvar}, 147
Gr.statusbar.show <nexp>, 147
Gr.text.align {{<type_nexp>}{,<paint_nexp>}}, 155
Gr.text.bold {{<lexp>}{,<paint_nexp>}}, 156
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 159
Gr.text.height {<height_nvar>} {, <up_nvar>} {, <down_nvar>}, 158
Gr.text.setfont {{<font_ptr_nexp>|<font_family_sexp>} {, <style_sexp>} {,<paint_nexp}}, 157
Gr.text.size {{<size_nexp>}{,<paint_nexp>}}, 156
Gr.text.skew {{<skew_nexp>}{,<paint_nexp>}}, 156
Gr.text.strike <lexp>, 156
Gr.text.typeface {{<nexp>} {, <style_nexp>} {,<paint_nexp>}}, 158
Gr.text.underline <lexp>, 156
Gr.text.width <nvar>, <exp>, 159
Gr.touch touched, x, y, 154
Gr.touch2 touched, x, y, 155
GR_COLLISION( <object_1_nvar>, <object_2_nvar>), 60
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 104
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 104
Headset <state_nvar>, <type_svar>, <mic_nvar>, 132
HEX$(<nexp>), 66
HEX(<sexp>), 59
Home, 136
Html.clear.cache, 111
Html.clear.history, 111
Html.close, 111
Html.get.datalink <data_svar>, 110
Html.go.back, 111
Html.go.forward, 111
Html.load.string <html_sexp>, 109
Html.load.url <file_sexp>, 109
Html.open {<ShowStatusBar_lexp> {, <Orientation_nexp>}}, 108
Html.orientation <nexp>, 109
Html.post <url_sexp>, <list_nexp>, 110
Http.post <url_sexp>, <list_nexp>, <result_svar>, 111
HYPOT(<nexp_x>, <nexp_y), 57
If / Then / Else, 76
If / Then / Else / Elseif / Endif, 76
Include FilePath, 81
Inkey$ <svar>, 94
Input {<prompt_sexp>}, <result_var>{, {<default_exp>}{,<canceled_nvar>}}, 93
INT$(<nexp>), 66
INT(<nexp>), 56
IS_IN(<sub_sexp>, <base_sexp>{, <start_nexp>}, 59
Join <source_array$[]>, <result_svar> {, <separator_sexp>{, <wrapper_sexp}}, 127
Join.all <source_array$[]>, <result_svar> {, <separator_sexp>{, <wrapper_sexp}}, 127
Kb.hide, 96
Kb.resume, 97
Kb.show, 96
Kb.showing <lvar>, 96
Kb.toggle, 96
Key.resume, 85
LEFT$(<sexp>, <count_nexp>), 62
LEN(<sexp>), 59
Let, 53
List.add <pointer_nexp>{, <exp>}..., 44
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 44
List.add.array string_list_pointer, Array$[{<start>,<length>}], 44
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 44
List.clear <pointer_nexp>, 45
List.create N|S, <pointer_nvar>, 43
List.get <pointer_nexp>,<index_nexp>, <svar>|<nvar>, 45
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 44
List.remove <pointer_nexp>,<index_nexp>, 45
List.replace <pointer_nexp>,<index_nexp>, <sexp>|<nexp>, 44
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 45
List.size <pointer_nexp>, <nvar>, 45
List.toArray <pointer_nexp>, Array$[] | Array[], 45
List.type <pointer_nexp>, <svar>, 45
LOG(<nexp>), 57
LOG10(<nexp>), 57
LOWER$(<sexp>), 66
MAX(<nexp>, <nexp>), 55
MenuKey.resume, 84
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 62
MIN(<nexp>, <nexp>), 55
Mkdir <path_sexp>, 100
MOD(<nexp1>, <nexp2>), 56
MyPhoneNumber <svar>, 121
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 132
OCT$(<nexp>), 67
OCT(<sexp>), 59
OnBackGround:, 136
OnBackKey:, 84
OnBtReadReady:, 120
OnConsoleTouch:, 84
OnError:, 83
OnGrTouch:, 155
OnKbChange:, 97
OnKeyPress:, 84, 85
OnMenuKey:, 84
OnTimer:, 124
Pause <ticks_nexp>, 134
Phone.call <sexp>, 121
Phone.dial <sexp>, 121
Phone.info <nvar>|<nexp>, 133
Phone.rcv.init, 121
Phone.rcv.next <state_nvar>, <number_svar>, 122
PI(), 57
Popup <message_sexp>{{, <x_nexp>}{, <y_nexp>}{, <duration_lexp>}}, 94
POW(<nexp1>, <nexp2>), 57
Print {<exp> {,|;}} ..., 90
RANDOMIZE(<nexp>), 55
Read.data <number>|<string> {,<number>|<string>...,<number>|<string>}, 85
Read.from <nexp>, 86
Read.next <var>, ..., 86
Rem, 50
Rename <old_path_sexp>, <new_path_sexp>, 100
REPLACE$( <sexp>, <argument_sexp>, <replace_sexp>), 63
RIGHT$(<sexp>, <count_nexp>), 62
Ringer.get.mode <nvar>, 126
Ringer.get.volume <vol_nvar> { , <max_nvar>, 126
Ringer.set.mode <nexp>, 126
Ringer.set.volume <nexp>, 126
RND(), 55
ROUND(<value_nexp>{, <count_nexp>{, <mode_sexp>}}), 56
Run <filename_sexp> {, <data_sexp>}, 82
Select <selection_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 95
Sensors.close, 183
Sensors.list <sensor_array$[]>, 182
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 182
Sensors.read sensor_type, p1, p2, p3, 183
SGN(<nexp>), 55
SHIFT(<value_nexp>, <bits_nexp>), 59
SIN(<nexp>), 58
SINH(<nexp>), 58
Sms.rcv.init, 122
Sms.rcv.next <svar>, 122
Sms.send <number_sexp>, <message_sexp>, 122
Socket.client.close, 114
Socket.client.connect <server_sexp>, <port_nexp> { , <wait_lexp> }, 112
Socket.client.read.file <file_nexp>, 113
Socket.client.read.line <line_svar>, 113
Socket.client.read.ready <nvar>, 113
Socket.client.server.ip <svar>, 113
Socket.client.status <status_nvar>, 113
Socket.client.write.bytes <sexp>, 114
Socket.client.write.file <file_nexp>, 114
Socket.client.write.line <line_sexp>, 113
Socket.myIP <svar>, 114
Socket.server.client.ip <nvar>, 116
Socket.server.close, 116
Socket.server.connect {<wait_lexp>}, 114
Socket.server.create <port_nexp>, 114
Socket.server.disconnect, 116
Socket.server.read.file <file_nexp>, 115
Socket.server.read.line <svar>, 115
Socket.server.read.ready <nvar>, 115
Socket.server.status <status_nvar>, 115
Socket.server.write.bytes <sexp>, 115
Socket.server.write.file <file_nexp>, 115
Socket.server.write.line <line_sexp>, 115
Soundpool.load <soundID_nvar>, <file_path_sexp>, 175
Soundpool.open <MaxStreams_nexp>, 175
Soundpool.pause <streamID_nexp>, 176
Soundpool.play <streamID_nvar>, <soundID_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 175
Soundpool.release, 176
Soundpool.resume <streamID_nexp>, 176
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 176
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 176
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 175
Soundpool.stop <streamID_nexp>, 176
Soundpool.unload <soundID_nexp>, 175
Split <result_array$[]>, <sexp> {, <test_sexp>}, 127
Split.all <result_array$[]>, <sexp> {, <test_sexp>}, 127
Sql.close <DB_pointer_nvar>, 137
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 139
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 138
Sql.exec <DB_pointer_nvar>, <command_sexp>, 140
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$, ...,CN$, VN$, 138
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 138
Sql.next <done_lvar>, <cursor_nvar>, C1V$, C2V$, .., CNV$, 139
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 137
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp> {, <order_sexp>} } }, 138
Sql.query.length <length_nvar>, <cursor_nvar>, 139
Sql.query.position <position_nvar>, <cursor_nvar>, 139
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 140
Sql.update <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN${: <where_sexp>, 140
SQR(<nexp>), 57
Stack.clear <ptr_nexp>, 50
Stack.create N|S, <ptr_nvar>, 49
Stack.isEmpty <ptr_nexp>, <nvar>, 49
Stack.peek <ptr_nexp>, <nvar>|<svar>, 49
Stack.pop <ptr_nexp>, <nvar>|<svar>, 49
Stack.push <ptr_nexp>, <nexp>|<sexp>, 49
Stack.type <ptr_nexp>, <svar>, 49
STARTS_WITH(<sub_sexp>, <base_sexp>{, <start_nexp>}, 60
STR$(<nexp>), 66
STT.listen, 129
STT.results <string_list_ptr_nexp>, 129
Su.close, 184
Su.open, 184
Su.read.line <svar>, 184
Su.read.ready <nvar>, 184
Su.write <sexp>, 184
Sw.begin <nexp>|<sexp>, 83
Sw.break, 83
Sw.case <nexp >|<sexp>, 83
Sw.default, 83
Sw.end, 83
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 134
System.close, 184
System.open, 183
System.read.line <svar>, 184
System.read.ready <nvar>, 184
System.write <sexp>, 184
TAN(<nexp>), 58
Text.close <file_table_nexp>, 102
Text.eof <file_table_nexp>, <lvar>, 103
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 95
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 102
Text.position.get <file_table_nexp>, <position_nvar>, 103
Text.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 104
Text.position.set <file_table_nexp>, <position_nexp>, 103
Text.readln <file_table_nexp> {,<svar>}..., 102
Text.writeln <file_table_nexp>, {<exp> {,|;}} ..., 103
TGet <result_svar>, <prompt_sexp> {, <title_sexp>}, 96
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 123
TIME(), 61
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 61
Timer.clear, 124
Timer.resume, 124
Timer.set <interval_nexp>, 124
TimeZone.get <tz_svar>, 124
TimeZone.list <tz_list_pointer_nexp>, 124
TimeZone.set { <tz_sexp> }, 123
TODEGREES(<nexp>), 58
Tone <frequency_nexp>, <duration_nexp>{, <duration_chk_lexp}, 134
TORADIANS(<nexp>), 58
TRIM$(<sexp>{, <test_sexp>}), 63
TTS.init, 128
TTS.speak <sexp> {, <wait_lexp>}, 128
TTS.speak.toFile <sexp> {, <path_sexp>}, 128
TTS.stop, 129
UCODE(<sexp>{, <index_nexp>}), 59
UnDim Array[], Array$[], ..., 39
UPPER$(<sexp>), 66
USING$({<locale_sexp>} , <format_sexp> { , <exp>}...), 67
VAL(<sexp>), 58
VERSION$(), 66
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 134
W_R.break, 78
W_R.continue, 78
WakeLock <code_nexp>, 135
While <lexp> / Repeat, 78
WiFi.info {{<SSID_svar>}{, <BSSID_svar>}{, <MAC_svar>}{, <IP_var>}{, <speed_nvar>}}, 136
WifiLock <code_nexp>, 135
WORD$(<source_sexp>, <n_nexp> {, <test_sexp>}), 63
