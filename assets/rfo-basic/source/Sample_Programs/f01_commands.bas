 There is 200+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 51
!! - Block Comment, 52
# - Format Line, 27
% - Middle of Line Comment, 52
? {<exp> {,|;}} ..., 94
ABS(<nexp>), 56
ACOS(<nexp>), 59
App.broadcast <action_sexp>, <data_uri_sexp>, <package_sexp>, <component_sexp>, <mime_type_sexp>, <categories_sexp>, <extras_bptr_nexp>, <flags_nexp>, 192
App.start <action_sexp>, <data_uri_sexp>, <package_sexp>, <component_sexp>, <mime_type_sexp>, <categories_sexp>, <extras_bptr_nexp>, <flags_nexp>, 193
Array.average <Average_nvar>, Array[{<start>,<length>}], 40
Array.copy SourceArray$[{<start>,<length>}], DestinationArray$[{{-}<extras>}], 40
Array.copy SourceArray[{<start>,<length>}], DestinationArray[{{-}<extras>}], 40
Array.delete Array[], Array$[] ..., 41
Array.dims Source[]{, {Dims[]}{, NumDims}}, 41
Array.fill Array$[{<start>,<length>}], <sexp>, 41
Array.fill Array[{<start>,<length>}], <nexp>, 41
Array.length n, Array$[{<start>,<length>}], 41
Array.length n, Array[{<start>,<length>}], 41
Array.load Array$[], <sexp>, ..., 41
Array.load Array[], <nexp>, ..., 41
Array.max <max_nvar> Array[{<start>,<length>}], 42
Array.min <min_nvar>, Array[{<start>,<length>}], 42
Array.reverse Array$[{<start>,<length>}], 42
Array.reverse Array[{<start>,<length>}], 42
Array.search Array$[{<start>,<length>}], <value_sexp>, <result_nvar>{,<start_nexp>}, 42
Array.search Array[{<start>,<length>}], <value_nexp>, <result_nvar>{,<start_nexp>}, 42
Array.shuffle Array[{<start>,<length>}], 42
Array.sort Array[{<start>,<length>}], 42
Array.std_dev <sd_nvar>, Array[{<start>,<length>}], 43
Array.sum <sum_nvar>, Array[{<start>,<length>}], 43
Array.variance <v_nvar>, Array[{<start>,<length>}], 43
ASCII(<sexp>{, <index_nexp>}), 61
ASIN(<nexp>), 59
ATAN(<nexp>), 59
ATAN2(<nexp_y>, <nexp_x>), 59
Audio.isdone <lvar>, 182
Audio.length <length_nvar>, <aft_nexp>, 181
Audio.load <aft_nvar>, <filename_sexp>, 180
Audio.loop, 181
Audio.pause, 181
Audio.play <aft_nexp>, 180
Audio.position.current <nvar>, 181
Audio.position.seek <nexp>, 181
Audio.record.start <fn_svar>, 182
Audio.record.stop, 182
Audio.release <aft_nexp>, 182
Audio.stop, 181
Audio.volume <left_nexp>, <right_nexp>, 181
Back.resume, 88
BACKGROUND(), 62
Background.resume, 139
BAND(<nexp1>, <nexp2>), 55
BIN$(<nexp>), 69
BIN(<sexp>), 60
BNOT(<nexp>, 56
BOR(<nexp1>, <nexp2>), 55
Browse <url_sexp>, 115
Bt.close, 123
Bt.connect {0|1}, 123
Bt.device.name <svar>, 125
Bt.disconnect, 123
Bt.onReadReady.resume, 124
Bt.open {0|1}, 122
Bt.read.bytes <svar>, 125
Bt.read.ready <nvar>, 124
Bt.reconnect, 123
Bt.set.UUID <sexp>, 125
Bt.status {{<connect_var>}{, <name_svar>}{, <address_svar>}}, 123
Bt.write {<exp> {,|;}} ..., 124
Bundle.clear <pointer_nexp>, 49
Bundle.contain <pointer_nexp>, <key_sexp> , <contains_nvar>, 49
Bundle.create <pointer_nvar>, 48
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 48
Bundle.keys <bundle_ptr_nexp>, <list_ptr_nexp>, 48
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 48
Bundle.remove <pointer_nexp>, <key_sexp>, 49
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 49
BXOR(<nexp1>, <nexp2>), 56
Byte.close <file_table_nexp>, 109
Byte.copy <file_table_nexp>,<output_file_sexp>, 112
Byte.eof <file_table_nexp>, <lvar>, 111
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 109
Byte.position.get <file_table_nexp>, <position_nexp>, 111
Byte.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 112
Byte.position.set <file_table_nexp>, <position_nexp>, 111
Byte.read.buffer <file_table_nexp>, <count_nexp>, <buffer_svar>, 111
Byte.read.byte <file_table_nexp> {,<nvar>}..., 109
Byte.read.number <file_table_nexp> {,<nvar>...}, 110
Byte.truncate <file_table_nexp>,<length_nexp>, 112
Byte.write.buffer <file_table_nexp>, <sexp>, 111
Byte.write.byte <file_table_nexp> {{,<nexp>}...{,<sexp>}}, 110
Byte.write.number <file_table_nexp> {,<nexp>}..., 110
Call <user_defined_function>, 77
CBRT(<nexp>), 58
CEIL(<nexp>), 57
CHR$(<nexp>, ...), 64
Clipboard.get <svar>, 129
Clipboard.put <sexp>, 129
CLOCK(), 62
Cls, 95
Console.front, 95
Console.line.count <count_nvar >, 95
Console.line.text <line_nexp>, <text_svar>, 95
Console.line.touched <line_nvar> {, <press_lvar>}, 95
Console.save <filename_sexp>, 95
Console.title { <title_sexp>}, 96
ConsoleTouch.resume, 88
COS(<nexp>), 59
COSH(<nexp>), 59
D_U.break, 81
D_U.continue, 81
Debug.dump.array Array[], 91
Debug.dump.bundle <bundlePtr_nexp>, 91
Debug.dump.list <listPtr_nexp>, 91
Debug.dump.scalars, 91
Debug.dump.stack <stackPtr_nexp>, 91
Debug.echo.off, 90
Debug.echo.on, 90
Debug.off, 90
Debug.on, 90
Debug.print, 91
Debug.show, 93
Debug.show.array Array[], 91
Debug.show.bundle <bundlePtr_nexp>, 92
Debug.show.list <listPtr_nexp>, 92
Debug.show.program, 92
Debug.show.scalars, 91
Debug.show.stack <stackPtr_nexp>, 92
Debug.show.watch, 92
Debug.watch var, ..., 92
DECODE$(<charset_sexp>, <buffer_sexp>), 68
DECODE$(<type_sexp>, {<qualifier_sexp>}, <source_sexp>), 66
Decrypt <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 129
Device <nexp>|<nvar>, 135
Device <svar>, 135
Dialog.message {<title_sexp>}, {<message_sexp>}, <sel_nvar> {, <button1_sexp>{, <button2_sexp>{, <button3_sexp>}}}, 96
Dialog.select <sel_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp>}, 97
Dim Array [n, n, ...], Array$[n, n, ...] ..., 40
Do / Until <lexp>, 80
Echo.off, 91
Echo.on, 90
Email.send <recipient_sexp>, <subject_sexp>, <body_sexp>, 125
ENCODE$(<charset_sexp>, <source_sexp>), 67
ENCODE$(<type_sexp>, {<qualifier_sexp>}, <source_sexp>), 66
Encrypt {<pw_sexp>}, <source_sexp>, <encrypted_svar>, 129
End{ <msg_sexp>}, 89
ENDS_WITH(<sub_sexp>, <base_sexp>), 62
Exit, 89
EXP(<nexp>), 58
F_N.break, 80
F_N.continue, 80
File.delete <lvar>, <path_sexp>, 104
File.dir <path_sexp>, Array$[] {, <dirmark_sexp>}, 104
File.exists <lvar>, <path_sexp>, 104
File.mkdir <path_sexp>, 104
File.rename <old_path_sexp>, <new_path_sexp>, 105
File.root <svar>, 105
File.size <size_nvar>, <path_sexp>, 105
File.type <type_svar>, <path_sexp>, 105
FLOOR(<nexp>), 57
Fn.def name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 76
Fn.end, 77
Fn.rtn <sexp>|<nexp>, 77
Font.clear, 93
Font.delete {<font_ptr_nexp>}, 93
Font.load <font_ptr_nvar>, <filename_sexp>, 93
For <nvar> = <nexp> To <nexp> {Step <nexp>} / Next, 79
FORMAT$(<pattern_sexp>, <nexp>), 73
FORMAT_USING$(<locale_sexp>, <format_sexp> { , <exp>}...), 73
FRAC(<nexp>), 57
Ftp.cd <new_directory_sexp>, 121
Ftp.close, 120
Ftp.delete <filename_sexp>, 121
Ftp.dir <list_nvar>, 121
Ftp.get <source_sexp>, <destination_sexp>, 121
Ftp.mkdir <directory_sexp>, 122
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 120
Ftp.put <source_sexp>, <destination_sexp>, 120
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 121
Ftp.rmdir <directory_sexp>, 122
GETERROR$(), 63
GoSub <index_nexp>, <label>... / Return, 83
GoSub <label> / Return, 82
GoTo <index_nexp>, <label>..., 82
GoTo <label>, 82
Gps.accuracy <nvar>, 189
Gps.altitude <nvar>, 189
Gps.bearing <nvar>, 189
Gps.close, 185
Gps.latitude <nvar>, 189
Gps.location {{<time_nvar>}, {<prov_svar>}, {<count_nvar}, {<acc_nvar>}, {<lat_nvar>}, {<long_nvar>}, {<alt_nvar>}, {<bear_nvar>}, {<speed_nvar>}}, 188
Gps.longitude <nvar>, 189
Gps.open {{<status_nvar>},{<time_nexp>},{<distance_nexp>}}, 184
Gps.provider <svar>, 188
Gps.satellites {{<count_nvar>}, {<sat_list_nexp>}}, 188
Gps.speed <nvar>, 189
Gps.status {{<status_var>}, {<infix_nvar>},{inview_nvar}, {<sat_list_nexp>}}, 185
Gps.time <nvar>, 188
Gr.arc <obj_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 157
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 168
Gr.bitmap.crop <new_bitmap_ptr_nvar>, <source_bitmap_ptr_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 169
Gr.bitmap.delete <bitmap_ptr_nexp>, 169
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nexp>, x , y, 170
Gr.bitmap.drawinto.end, 171
Gr.bitmap.drawinto.start <bitmap_ptr_nexp>, 170
Gr.bitmap.fill <bitmap_ptr_nexp>, <x_nexp>, <y_nexp>, 170
Gr.bitmap.load <bitmap_ptr_nvar>, <file_name_sexp>, 168
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 170
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nexp>, width, height {, <smoothing_lexp> }, 169
Gr.bitmap.size <bitmap_ptr_nexp>, width, height, 169
Gr.bounded.touch touched, left, top, right, bottom, 162
Gr.bounded.touch2 touched, left, top, right, bottom, 162
Gr.brightness <nexp>, 156
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 174
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 174
Gr.camera.select 1|2, 173
Gr.camera.shoot <bm_ptr_nvar>, 173
Gr.circle <obj_nvar>, x, y, radius, 157
Gr.clip <object__ptr_nexp>, <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 178
Gr.close, 155
Gr.cls, 155
Gr.color {{alpha}{, red}{, green}{, blue}{, style}{, paint}}, 152
Gr.front flag, 155
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 170
Gr.get.params <object_ptr_nexp>, <param_array$[]>, 175
Gr.get.pixel x, y, alpha, red, green, blue, 175
Gr.get.position <object_ptr_nexp>, x, y, 175
Gr.get.textbounds <exp>, left, top, right, bottom, 166
Gr.get.type <object_ptr_nexp>, <type_svar>, 175
Gr.get.value <object_ptr_nexp> {, <tag_sexp>, <value_nvar | value_svar>}..., 176
Gr.getDL <dl_array[]> {, <keep_all_objects_lexp> }, 179
Gr.group <object_number_nvar>{, <obj_nexp>}..., 160
Gr.group.getDL <object_number_nvar>, 160
Gr.group.list <object_number_nvar>, <list_ptr_nexp>, 160
Gr.group.newDL <object_number_nvar>, 160
Gr.hide <object_number_nexp>, 160
Gr.line <obj_nvar>, x1, y1, x2, y2, 156
Gr.modify <object_ptr_nexp> {, <tag_sexp>, <value_nexp | value_sexp>}..., 176
Gr.move <object_ptr_nexp>{{, dx}{, dy}}, 175
Gr.newDL <dl_array[{<start>,<length>}]>, 179
Gr.onGrTouch.resume, 162
Gr.open {{alpha}{, red}{, green}{, blue}{, <ShowStatusBar_lexp>}{, <Orientation_nexp>}}, 151
Gr.orientation <nexp>, 153
Gr.oval <obj_nvar>, left, top, right, bottom, 157
Gr.paint.copy {{<src_nexp>}{, <dst_nexp>}}, 171
Gr.paint.get <object_ptr_nvar>, 171
Gr.paint.reset {<nexp>}, 172
Gr.point <obj_nvar>, x, y, 156
Gr.poly <obj_nvar>, list_pointer {,x,y}, 158
Gr.rect <obj_nvar>, left, top, right, bottom, 157
Gr.render, 154
Gr.rotate.end {<obj_nvar>}, 172
Gr.rotate.start angle, x, y{,<obj_nvar>}, 172
Gr.save <filename_sexp> {,<quality_nexp>}, 175
Gr.scale x_factor, y_factor, 155
Gr.screen width, height{, density }, 154
Gr.screen.to_bitmap <bm_ptr_nvar>, 174
Gr.set.antialias {{<lexp>}{,<paint_nexp>}}, 153
Gr.set.pixels <obj_nvar>, pixels[{<start>,<length>}] {,x,y}, 158
Gr.set.stroke {{<nexp>}{,<paint_nexp>}}, 153
Gr.show <object_number_nexp>, 161
Gr.show.toggle <object_number_nexp>, 161
Gr.statusbar {<height_nvar>} {, showing_lvar}, 154
Gr.statusbar.show <nexp>, 154
Gr.text.align {{<type_nexp>}{,<paint_nexp>}}, 163
Gr.text.bold {{<lexp>}{,<paint_nexp>}}, 163
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 167
Gr.text.height {<height_nvar>} {, <up_nvar>} {, <down_nvar>}, 165
Gr.text.setfont {{<font_ptr_nexp>|<font_family_sexp>} {, <style_sexp>} {,<paint_nexp}}, 164
Gr.text.size {{<size_nexp>}{,<paint_nexp>}}, 163
Gr.text.skew {{<skew_nexp>}{,<paint_nexp>}}, 163
Gr.text.strike {{<lexp>}{,<paint_nexp>}}, 163
Gr.text.typeface {{<nexp>} {, <style_nexp>} {,<paint_nexp>}}, 165
Gr.text.underline {{<lexp>}{<paint_nexp>}}, 164
Gr.text.width <nvar>, <exp>, 166
Gr.touch touched, x, y, 161
Gr.touch2 touched, x, y, 162
GR_COLLISION( <object_1_nvar>, <object_2_nvar>), 62
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 108
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 108
Headset <state_nvar>, <type_svar>, <mic_nvar>, 140
HEX$(<nexp>), 68
HEX(<sexp>), 60
Home, 139
Html.clear.cache, 115
Html.clear.history, 115
Html.close, 115
Html.get.datalink <data_svar>, 114
Html.go.back, 115
Html.go.forward, 115
Html.load.string <html_sexp>, 114
Html.load.url <file_sexp>, 113
Html.open {<ShowStatusBar_lexp> {, <Orientation_nexp>}}, 113
Html.orientation <nexp>, 113
Html.post <url_sexp>, <list_nexp>, 114
Http.post <url_sexp>, <list_nexp>, <result_svar>, 116
HYPOT(<nexp_x>, <nexp_y), 59
If / Then / Else, 78
If / Then / Else / Elseif / Endif, 78
Include FilePath, 83
Inkey$ <svar>, 98
Input {<prompt_sexp>}, <result_var>{, {<default_exp>}{,<canceled_nvar>}}, 97
INT$(<nexp>), 68
INT(<nexp>), 57
IS_IN(<sub_sexp>, <base_sexp>{, <start_nexp>}, 61
IS_NUMBER(<sexp>), 60
Join <source_array$[]>, <result_svar> {, <separator_sexp>{, <wrapper_sexp}}, 131
Join.all <source_array$[]>, <result_svar> {, <separator_sexp>{, <wrapper_sexp}}, 131
Kb.hide, 100
Kb.resume, 101
Kb.show, 100
Kb.showing <lvar>, 100
Kb.toggle, 100
Key.resume, 89
LEFT$(<sexp>, <count_nexp>), 64
LEN(<sexp>), 60
Let, 54
List.add <pointer_nexp>{, <exp>}..., 45
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 45
List.add.array string_list_pointer, Array$[{<start>,<length>}], 45
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 45
List.clear <pointer_nexp>, 46
List.create N|S, <pointer_nvar>, 45
List.get <pointer_nexp>, <index_nexp>, <var>, 46
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 46
List.remove <pointer_nexp>,<index_nexp>, 46
List.replace <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 46
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 46
List.size <pointer_nexp>, <nvar>, 46
List.toArray <pointer_nexp>, Array$[] | Array[], 47
List.type <pointer_nexp>, <svar>, 46
LOG(<nexp>), 58
LOG10(<nexp>), 58
LOWER$(<sexp>), 68
LowMemory.resume, 89
MAX(<nexp>, <nexp>), 57
MenuKey.resume, 88
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 64
MIN(<nexp>, <nexp>), 57
mkdir <path_sexp>, 104
MOD(<nexp1>, <nexp2>), 57
MyPhoneNumber <svar>, 125
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 141
OCT$(<nexp>), 69
OCT(<sexp>), 60
OnBackground:, 139
OnBackKey:, 88
OnBtReadReady:, 124
OnConsoleTouch:, 88
OnError:, 87
OnGrTouch:, 162
OnKbChange:, 101
OnKeyPress:, 88
OnLowMemory:, 89
OnMenuKey:, 88
OnTimer:, 128
Pause <ticks_nexp>, 141
Phone.call <sexp>, 125
Phone.dial <sexp>, 126
Phone.info <nexp>|<nvar>, 136
Phone.rcv.init, 126
Phone.rcv.next <state_nvar>, <number_svar>, 126
PI(), 59
Popup <message_sexp>{{, <x_nexp>}{, <y_nexp>}{, <duration_lexp>}}, 98
POW(<nexp1>, <nexp2>), 58
Print {<exp> {,|;}} ..., 94
RANDOMIZE({<nexp>}), 56
Read.data <number>|<string> {,<number>|<string>...,<number>|<string>}, 89
Read.from <nexp>, 90
Read.next <var>, ..., 89
Rem, 51
REPLACE$( <sexp>, <argument_sexp>, <replace_sexp>), 65
RIGHT$(<sexp>, <count_nexp>), 64
Ringer.get.mode <nvar>, 130
Ringer.get.volume <vol_nvar> { , <max_nvar>, 130
Ringer.set.mode <nexp>, 130
Ringer.set.volume <nexp>, 130
RND(), 56
ROUND(<value_nexp>{, <count_nexp>{, <mode_sexp>}}), 57
Run <filename_sexp> {, <data_sexp>}, 84
Screen rotation, size[], realsize[], density, 137
Screen.rotation <nvar>, 138
Screen.size size[], realsize[], density, 138
Select <sel_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 99
Sensors.close, 191
Sensors.list <sensor_array$[]>, 190
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 190
Sensors.read sensor_type, p1, p2, p3, 191
SGN(<nexp>), 56
SHIFT(<value_nexp>, <bits_nexp>), 61
SIN(<nexp>), 59
SINH(<nexp>), 59
Sms.rcv.init, 126
Sms.rcv.next <svar>, 126
Sms.send <number_sexp>, <message_sexp>, 126
Socket.client.close, 118
Socket.client.connect <server_sexp>, <port_nexp> { , <wait_lexp> }, 117
Socket.client.read.file <file_nexp>, 117
Socket.client.read.line <line_svar>, 117
Socket.client.read.ready <nvar>, 117
Socket.client.server.ip <svar>, 117
Socket.client.status <status_nvar>, 117
Socket.client.write.bytes <sexp>, 118
Socket.client.write.file <file_nexp>, 118
Socket.client.write.line <line_sexp>, 118
Socket.myIP <array$[]>{, <nvar>}, 118
Socket.myIP <svar>, 118
Socket.server.client.ip <nvar>, 120
Socket.server.close, 120
Socket.server.connect {<wait_lexp>}, 119
Socket.server.create <port_nexp>, 119
Socket.server.disconnect, 120
Socket.server.read.file <file_nexp>, 120
Socket.server.read.line <svar>, 119
Socket.server.read.ready <nvar>, 119
Socket.server.status <status_nvar>, 119
Socket.server.write.bytes <sexp>, 119
Socket.server.write.file <file_nexp>, 120
Socket.server.write.line <line_sexp>, 119
Soundpool.load <soundID_nvar>, <file_path_sexp>, 183
Soundpool.open <MaxStreams_nexp>, 183
Soundpool.pause <streamID_nexp>, 184
Soundpool.play <streamID_nvar>, <soundID_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 183
Soundpool.release, 184
Soundpool.resume <streamID_nexp>, 184
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 184
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 184
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 183
Soundpool.stop <streamID_nexp>, 184
Soundpool.unload <soundID_nexp>, 183
Split <result_array$[]>, <sexp> {, <test_sexp>}, 131
Split.all <result_array$[]>, <sexp> {, <test_sexp>}, 131
Sql.close <DB_pointer_nvar>, 144
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 146
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 144
Sql.exec <DB_pointer_nvar>, <command_sexp>, 147
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$, ...,CN$, VN$, 144
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 144
Sql.next doneFlag, cursorVar {{, svar}...{, array$[]{, nColVar}}}, 145
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 143
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp> {, <order_sexp>} }, 145
Sql.query.length <length_nvar>, <cursor_nvar>, 145
Sql.query.position <position_nvar>, <cursor_nvar>, 145
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 147
Sql.update <DB_ptr_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN${: <where_sexp>}, 146
SQR(<nexp>), 58
Stack.clear <ptr_nexp>, 51
Stack.create N|S, <ptr_nvar>, 50
Stack.isEmpty <ptr_nexp>, <nvar>, 51
Stack.peek <ptr_nexp>, <nvar>|<svar>, 50
Stack.pop <ptr_nexp>, <nvar>|<svar>, 50
Stack.push <ptr_nexp>, <nexp>|<sexp>, 50
Stack.type <ptr_nexp>, <svar>, 51
STARTS_WITH(<sub_sexp>, <base_sexp>{, <start_nexp>}, 61
STR$(<nexp>), 68
STT.listen, 133
STT.results <string_list_ptr_nexp>, 133
Su.close, 192
Su.open, 192
Su.read.line <svar>, 192
Su.read.ready <nvar>, 192
Su.write <sexp>, 192
Sw.begin <exp>, 85
Sw.break, 86
Sw.case <exp >, ..., 85
Sw.case <op><exp >, 86
Sw.default, 86
Sw.end, 86
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 142
System.close, 192
System.open, 191
System.read.line <svar>, 192
System.read.ready <nvar>, 192
System.write <sexp>, 192
TAN(<nexp>), 59
Text.close <file_table_nexp>, 106
Text.eof <file_table_nexp>, <lvar>, 107
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 99
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 106
Text.position.get <file_table_nexp>, <position_nvar>, 107
Text.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 108
Text.position.set <file_table_nexp>, <position_nexp>, 107
Text.readln <file_table_nexp> {,<svar>}..., 106
Text.writeln <file_table_nexp>, {<exp> {,|;}} ..., 107
TGet <result_svar>, <prompt_sexp> {, <title_sexp>}, 100
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 127
TIME(), 62
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 63
Timer.clear, 129
Timer.resume, 128
Timer.set <interval_nexp>, 128
TimeZone.get <tz_svar>, 128
TimeZone.list <tz_list_pointer_nexp>, 128
TimeZone.set { <tz_sexp> }, 128
TODEGREES(<nexp>), 59
Tone <frequency_nexp>, <duration_nexp>{, <duration_chk_lexp}, 142
TORADIANS(<nexp>), 60
TRIM$(<sexp>{, <test_sexp>}), 65
TTS.init, 132
TTS.speak <sexp> {, <wait_lexp>}, 133
TTS.speak.toFile <sexp> {, <path_sexp>}, 133
TTS.stop, 133
UCODE(<sexp>{, <index_nexp>}), 61
UnDim Array[], Array$[], ..., 40
UPPER$(<sexp>), 68
USING$({<locale_sexp>} , <format_sexp> { , <exp>}...), 69
VAL(<sexp>), 60
VERSION$(), 68
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 142
VolKeys.off, 143
VolKeys.on, 143
W_R.break, 80
W_R.continue, 80
WakeLock <code_nexp>, 139
While <lexp> / Repeat, 80
WiFi.info {{<SSID_svar>}{, <BSSID_svar>}{, <MAC_svar>}{, <IP_var>}{, <speed_nvar>}}, 138
WifiLock <code_nexp>, 140
WORD$(<source_sexp>, <n_nexp> {, <test_sexp>}), 65
