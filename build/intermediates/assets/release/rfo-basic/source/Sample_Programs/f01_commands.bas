 There is 200+ page manual that gives detailed information about each of these commands. The number at the end of each line is the manual page number for the command. 
 Press Menu->About to find this manual.
 Press an alpha key to scroll to commands that start with that letter. (Does not work on all devices.)
 Tap a command's line. The command will be loaded into the clip board. It can then be pasted into the Editor.
*
! - Single Line Comment, 52
!! - Block Comment, 52
# - Format Line, 27
% - Middle of Line Comment, 52
? {<exp> {,|;}} ..., 96
ABS(<nexp>), 56
ACOS(<nexp>), 60
App.broadcast <action_sexp>, <data_uri_sexp>, <package_sexp>, <component_sexp>, <mime_type_sexp>, <categories_sexp>, <extras_bptr_nexp>, <flags_nexp>, 199
App.start <action_sexp>, <data_uri_sexp>, <package_sexp>, <component_sexp>, <mime_type_sexp>, <categories_sexp>, <extras_bptr_nexp>, <flags_nexp>, 199
Array.average <Average_nvar>, Array[{<start>,<length>}], 41
Array.copy SourceArray$[{<start>,<length>}], DestinationArray$[{{-}<start_or_extras>}], 41
Array.copy SourceArray[{<start>,<length>}], DestinationArray[{{-}<start_or_extras>}], 41
Array.delete Array[], Array$[] ..., 41
Array.dims Source[]{, {Dims[]}{, NumDims}}, 41
Array.fill Array$[{<start>,<length>}], <sexp>, 42
Array.fill Array[{<start>,<length>}], <nexp>, 42
Array.length n, Array$[{<start>,<length>}], 42
Array.length n, Array[{<start>,<length>}], 42
Array.load Array$[], <sexp>, ..., 42
Array.load Array[], <nexp>, ..., 42
Array.max <max_nvar> Array[{<start>,<length>}], 42
Array.min <min_nvar>, Array[{<start>,<length>}], 42
Array.reverse Array$[{<start>,<length>}], 43
Array.reverse Array[{<start>,<length>}], 43
Array.search Array$[{<start>,<length>}], <value_sexp>, <result_nvar>{,<start_nexp>}, 43
Array.search Array[{<start>,<length>}], <value_nexp>, <result_nvar>{,<start_nexp>}, 43
Array.shuffle Array[{<start>,<length>}], 43
Array.sort Array[{<start>,<length>}], 43
Array.std_dev <sd_nvar>, Array[{<start>,<length>}], 43
Array.sum <sum_nvar>, Array[{<start>,<length>}], 43
Array.variance <v_nvar>, Array[{<start>,<length>}], 43
ASCII(<sexp>{, <index_nexp>}), 61
ASIN(<nexp>), 60
ATAN(<nexp>), 60
ATAN2(<nexp_y>, <nexp_x>), 60
Audio.isdone <lvar>, 188
Audio.length <length_nvar>, <aft_nexp>, 188
Audio.load <aft_nvar>, <filename_sexp>, 186
Audio.loop, 187
Audio.pause, 187
Audio.play <aft_nexp>, 186
Audio.position.current <nvar>, 187
Audio.position.seek <nexp>, 188
Audio.record.start <fn_svar>, 188
Audio.record.stop, 188
Audio.release <aft_nexp>, 188
Audio.stop, 187
Audio.volume <left_nexp>, <right_nexp>, 187
Back.resume, 90
BACKGROUND(), 62
Background.resume, 145
BAND(<nexp1>, <nexp2>), 56
BIN$(<nexp>), 69
BIN(<sexp>), 61
BNOT(<nexp>, 56
BOR(<nexp1>, <nexp2>), 56
Browse <url_sexp>, 120
Bt.close, 128
Bt.connect {0|1}, 128
Bt.device.name <svar>, 130
Bt.disconnect, 128
Bt.onReadReady.resume, 129
Bt.open {0|1}, 127
Bt.read.bytes <svar>, 130
Bt.read.ready <nvar>, 129
Bt.reconnect, 128
Bt.set.UUID <sexp>, 130
Bt.status {{<connect_var>}{, <name_svar>}{, <address_svar>}}, 128
Bt.write {<exp> {,|;}} ..., 129
Bundle.clear <pointer_nexp>, 50
Bundle.contain <pointer_nexp>, <key_sexp> , <contains_nvar>, 50
Bundle.create <pointer_nvar>, 48
Bundle.get <pointer_nexp>, <key_sexp>, <nvar>|<svar>, 49
Bundle.keys <bundle_ptr_nexp>, <list_ptr_nexp>, 49
Bundle.put <pointer_nexp>, <key_sexp>, <value_nexp>|<value_sexp>, 49
Bundle.remove <pointer_nexp>, <key_sexp>, 50
Bundle.type <pointer_nexp>, <key_sexp>, <type_svar>, 50
BXOR(<nexp1>, <nexp2>), 56
Byte.close <file_table_nexp>, 112
Byte.copy <file_table_nexp>,<output_file_sexp>, 115
Byte.eof <file_table_nexp>, <lvar>, 114
Byte.open {r|w|a}, <file_table_nvar>, <path_sexp>, 112
Byte.position.get <file_table_nexp>, <position_nexp>, 114
Byte.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 115
Byte.position.set <file_table_nexp>, <position_nexp>, 114
Byte.read.buffer <file_table_nexp>, <count_nexp>, <buffer_svar>, 114
Byte.read.byte <file_table_nexp> {,<nvar>}..., 112
Byte.read.number <file_table_nexp> {,<nvar>...}, 113
Byte.truncate <file_table_nexp>,<length_nexp>, 115
Byte.write.buffer <file_table_nexp>, <sexp>, 114
Byte.write.byte <file_table_nexp> {{,<nexp>}...{,<sexp>}}, 113
Byte.write.number <file_table_nexp> {,<nexp>}..., 113
Call <user_defined_function>, 78
CBRT(<nexp>), 59
CEIL(<nexp>), 57
CHR$(<nexp>, ...), 64
Clipboard.get <svar>, 134
Clipboard.put <sexp>, 134
CLOCK(), 63
Cls, 97
Console.front, 97
Console.line.count <count_nvar >, 97
Console.line.text <line_nexp>, <text_svar>, 97
Console.line.touched <line_nvar> {, <press_lvar>}, 98
Console.save <filename_sexp>, 98
Console.title { <title_sexp>}, 98
ConsoleTouch.resume, 90
COS(<nexp>), 59
COSH(<nexp>), 60
D_U.break, 82
D_U.continue, 81
Debug.dump.array Array[], 93
Debug.dump.bundle <bundlePtr_nexp>, 93
Debug.dump.list <listPtr_nexp>, 93
Debug.dump.scalars, 93
Debug.dump.stack <stackPtr_nexp>, 93
Debug.echo.off, 93
Debug.echo.on, 92
Debug.off, 92
Debug.on, 92
Debug.print, 93
Debug.show, 95
Debug.show.array Array[], 94
Debug.show.bundle <bundlePtr_nexp>, 94
Debug.show.list <listPtr_nexp>, 94
Debug.show.program, 94
Debug.show.scalars, 93
Debug.show.stack <stackPtr_nexp>, 94
Debug.show.watch, 94
Debug.watch var, ..., 94
DECODE$(<charset_sexp>, <buffer_sexp>), 68
DECODE$(<type_sexp>, {<qualifier_sexp>}, <source_sexp>), 67
Decrypt <pw_sexp>, <encrypted_svar>, <decrypted_svar>, 135
Device <nexp>|<nvar>, 141
Device <svar>, 141
Device.Language <svar>, 141
Device.Locale <svar>, 142
Dialog.message {<title_sexp>}, {<message_sexp>}, <sel_nvar> {, <button1_sexp>{, <button2_sexp>{, <button3_sexp>}}}, 98
Dialog.select <sel_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp>}, 99
Dim Array [n, n, ...], Array$[n, n, ...] ..., 40
Do / Until <lexp>, 81
Echo.off, 93
Echo.on, 93
Email.send <recipient_sexp>, <subject_sexp>, <body_sexp>, 130
ENCODE$(<charset_sexp>, <source_sexp>), 68
ENCODE$(<type_sexp>, {<qualifier_sexp>}, <source_sexp>), 66
Encrypt {<pw_sexp>}, <source_sexp>, <encrypted_svar>, 134
End{ <msg_sexp>}, 91
ENDS_WITH(<sub_sexp>, <base_sexp>), 62
Exit, 91
EXP(<nexp>), 59
F_N.break, 80
F_N.continue, 80
File.delete <lvar>, <path_sexp>, 106
File.dir <path_sexp>, Array$[] {, <dirmark_sexp>}, 107
File.exists <lvar>, <path_sexp>, 107
File.mkdir <path_sexp>, 107
File.rename <old_path_sexp>, <new_path_sexp>, 107
File.root <svar>, 108
File.size <size_nvar>, <path_sexp>, 108
File.type <type_svar>, <path_sexp>, 108
FLOOR(<nexp>), 57
Fn.def name|name$( {nvar}|{svar}|Array[]|Array$[], ... {nvar}|{svar}|Array[]|Array$[]), 76
Fn.end, 78
Fn.rtn <sexp>|<nexp>, 77
Font.clear, 95
Font.delete {<font_ptr_nexp>}, 95
Font.load <font_ptr_nvar>, <filename_sexp>, 95
For <nvar> = <nexp> To <nexp> {Step <nexp>} / Next, 79
FORMAT$(<pattern_sexp>, <nexp>), 74
FORMAT_USING$(<locale_sexp>, <format_sexp> { , <exp>}...), 74
FRAC(<nexp>), 57
Ftp.cd <new_directory_sexp>, 126
Ftp.close, 125
Ftp.delete <filename_sexp>, 126
Ftp.dir <list_nvar>, 126
Ftp.get <source_sexp>, <destination_sexp>, 126
Ftp.mkdir <directory_sexp>, 127
Ftp.open <url_sexp>, <port_nexp>, <user_sexp>, <pw_sexp>, 125
Ftp.put <source_sexp>, <destination_sexp>, 125
Ftp.rename <old_filename_sexp>, <new_filename_sexp>, 126
Ftp.rmdir <directory_sexp>, 126
GETERROR$(), 63
GoSub <index_nexp>, <label>... / Return, 83
GoSub <label> / Return, 83
GoTo <index_nexp>, <label>..., 83
GoTo <label>, 83
Gps.accuracy <nvar>, 195
Gps.altitude <nvar>, 195
Gps.bearing <nvar>, 195
Gps.close, 191
Gps.latitude <nvar>, 195
Gps.location {{<time_nvar>}, {<prov_svar>}, {<count_nvar}, {<acc_nvar>}, {<lat_nvar>}, {<long_nvar>}, {<alt_nvar>}, {<bear_nvar>}, {<speed_nvar>}}, 194
Gps.longitude <nvar>, 195
Gps.open {{<status_nvar>},{<time_nexp>},{<distance_nexp>}}, 191
Gps.provider <svar>, 194
Gps.satellites {{<count_nvar>}, {<sat_list_nexp>}}, 195
Gps.speed <nvar>, 195
Gps.status {{<status_var>}, {<infix_nvar>},{inview_nvar}, {<sat_list_nexp>}}, 191
Gps.time <nvar>, 194
Gr.arc <obj_nvar>, left, top, right, bottom, start_angle, sweep_angle, fill_mode, 163
Gr.bitmap.create <bitmap_ptr_nvar>, width, height, 174
Gr.bitmap.crop <new_bitmap_ptr_nvar>, <source_bitmap_ptr_nexp>, <x_nexp>, <y_nexp>, <width_nexp>, <height_nexp>, 175
Gr.bitmap.delete <bitmap_ptr_nexp>, 175
Gr.bitmap.draw <object_ptr_nvar>, <bitmap_ptr_nexp>, x , y, 176
Gr.bitmap.drawinto.end, 177
Gr.bitmap.drawinto.start <bitmap_ptr_nexp>, 176
Gr.bitmap.fill <bitmap_ptr_nexp>, <x_nexp>, <y_nexp>, 176
Gr.bitmap.load <bitmap_ptr_nvar>, <file_name_sexp>, 174
Gr.bitmap.save <bitmap_ptr_nvar>, <filename_sexp>{, <quality_nexp>}, 176
Gr.bitmap.scale <new_bitmap_ptr_nvar>, <bitmap_ptr_nexp>, width, height {, <smoothing_lexp> }, 175
Gr.bitmap.size <bitmap_ptr_nexp>, width, height, 175
Gr.bounded.touch touched, left, top, right, bottom, 168
Gr.bounded.touch2 touched, left, top, right, bottom, 168
Gr.brightness <nexp>, 162
Gr.camera.autoshoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 180
Gr.camera.manualShoot <bm_ptr_nvar>{, <flash_ mode_nexp> {, focus_mode_nexp} }, 180
Gr.camera.select 1|2, 179
Gr.camera.shoot <bm_ptr_nvar>, 179
Gr.circle <obj_nvar>, x, y, radius, 164
Gr.clip <object__ptr_nexp>, <left_nexp>,<top_nexp>, <right_nexp>, <bottom_nexp>{, <RO_nexp>}, 184
Gr.close, 162
Gr.cls, 161
Gr.color {{alpha}{, red}{, green}{, blue}{, style}{, paint}}, 158
Gr.front flag, 162
Gr.get.bmpixel <bitmap_ptr_nvar>, x, y, alpha, red, green, blue, 176
Gr.get.params <object_ptr_nexp>, <param_array$[]>, 181
Gr.get.pixel x, y, alpha, red, green, blue, 181
Gr.get.position <object_ptr_nexp>, x, y, 181
Gr.get.textbounds <exp>, left, top, right, bottom, 172
Gr.get.type <object_ptr_nexp>, <type_svar>, 181
Gr.get.value <object_ptr_nexp> {, <tag_sexp>, <value_nvar | value_svar>}..., 182
Gr.getDL <dl_array[]> {, <keep_all_objects_lexp> }, 185
Gr.group <object_number_nvar>{, <obj_nexp>}..., 166
Gr.group.getDL <object_number_nvar>, 166
Gr.group.list <object_number_nvar>, <list_ptr_nexp>, 166
Gr.group.newDL <object_number_nvar>, 166
Gr.hide <object_number_nexp>, 167
Gr.line <obj_nvar>, x1, y1, x2, y2, 163
Gr.modify <object_ptr_nexp> {, <tag_sexp>, <value_nexp | value_sexp>}..., 182
Gr.move <object_ptr_nexp>{{, dx}{, dy}}, 181
Gr.newDL <dl_array[{<start>,<length>}]>, 185
Gr.onGrTouch.resume, 168
Gr.open {{alpha}{, red}{, green}{, blue}{, <ShowStatusBar_lexp>}{, <Orientation_nexp>}}, 158
Gr.orientation <nexp>, 160
Gr.oval <obj_nvar>, left, top, right, bottom, 163
Gr.paint.copy {{<src_nexp>}{, <dst_nexp>}}, 177
Gr.paint.get <object_ptr_nvar>, 177
Gr.paint.reset {<nexp>}, 178
Gr.point <obj_nvar>, x, y, 162
Gr.poly <obj_nvar>, list_pointer {,x,y}, 164
Gr.rect <obj_nvar>, left, top, right, bottom, 163
Gr.render, 160
Gr.rotate.end {<obj_nvar>}, 178
Gr.rotate.start angle, x, y{,<obj_nvar>}, 178
Gr.save <filename_sexp> {,<quality_nexp>}, 181
Gr.scale x_factor, y_factor, 161
Gr.screen width, height{, density }, 161
Gr.screen.to_bitmap <bm_ptr_nvar>, 180
Gr.set.antialias {{<lexp>}{,<paint_nexp>}}, 159
Gr.set.pixels <obj_nvar>, pixels[{<start>,<length>}] {,x,y}, 164
Gr.set.stroke {{<nexp>}{,<paint_nexp>}}, 159
Gr.show <object_number_nexp>, 167
Gr.show.toggle <object_number_nexp>, 167
Gr.statusbar {<height_nvar>} {, showing_lvar}, 160
Gr.statusbar.show <nexp>, 160
Gr.text.align {{<type_nexp>}{,<paint_nexp>}}, 169
Gr.text.bold {{<lexp>}{,<paint_nexp>}}, 169
Gr.text.draw <object_number_nvar>, <x_nexp>, <y_nexp>, <text_object_sexp>, 173
Gr.text.height {<height_nvar>} {, <up_nvar>} {, <down_nvar>}, 171
Gr.text.setfont {{<font_ptr_nexp>|<font_family_sexp>} {, <style_sexp>} {,<paint_nexp>}}, 170
Gr.text.size {{<size_nexp>}{,<paint_nexp>}}, 169
Gr.text.skew {{<skew_nexp>}{,<paint_nexp>}}, 169
Gr.text.strike {{<lexp>}{,<paint_nexp>}}, 170
Gr.text.typeface {{<nexp>} {, <style_nexp>} {,<paint_nexp>}}, 171
Gr.text.underline {{<lexp>}{,<paint_nexp>}}, 170
Gr.text.width <nvar>, <exp>, 172
Gr.touch touched, x, y, 167
Gr.touch2 touched, x, y, 168
GR_COLLISION( <object_1_nvar>, <object_2_nvar>), 62
GrabFile <result_svar>, <path_sexp>{, <unicode_flag_lexp>}, 111
GrabURL <result_svar>, <url_sexp>{, <timeout_nexp>}, 111
Headset <state_nvar>, <type_svar>, <mic_nvar>, 147
HEX$(<nexp>), 69
HEX(<sexp>), 61
Home, 145
Html.clear.cache, 120
Html.clear.history, 120
Html.close, 120
Html.get.datalink <data_svar>, 119
Html.go.back, 120
Html.go.forward, 120
Html.load.string <html_sexp>, 118
Html.load.url <file_sexp>, 118
Html.open {<ShowStatusBar_lexp> {, <Orientation_nexp>}}, 117
Html.orientation <nexp>, 118
Html.post <url_sexp>, <list_nexp>, 119
Http.post <url_sexp>, <list_nexp>, <result_svar>, 120
HYPOT(<nexp_x>, <nexp_y), 59
If / Then / Else, 79
If / Then / Else / Elseif / Endif, 78
Include FilePath, 84
Inkey$ <svar>, 100
Input {<prompt_sexp>}, <result_var>{, {<default_exp>}{,<canceled_nvar>}}, 100
INT$(<nexp>), 69
INT(<nexp>), 57
IS_IN(<sub_sexp>, <base_sexp>{, <start_nexp>}, 61
IS_NUMBER(<sexp>), 60
Join <source_array$[]>, <result_svar> {, <separator_sexp>{, <wrapper_sexp}}, 136
Join.all <source_array$[]>, <result_svar> {, <separator_sexp>{, <wrapper_sexp}}, 136
Kb.hide, 102
Kb.resume, 103
Kb.show, 102
Kb.showing <lvar>, 103
Kb.toggle, 103
Key.resume, 91
LEFT$(<sexp>, <count_nexp>), 64
LEN(<sexp>), 61
Let, 55
List.add <pointer_nexp>{, <exp>}..., 45
List.add.array numeric_list_pointer, Array[{<start>,<length>}], 46
List.add.array string_list_pointer, Array$[{<start>,<length>}], 46
List.add.list <destination_list_pointer_nexp>, <source_list_pointer_nexp>, 46
List.clear <pointer_nexp>, 47
List.create N|S, <pointer_nvar>, 45
List.get <pointer_nexp>, <index_nexp>, <var>, 47
List.insert <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 46
List.remove <pointer_nexp>,<index_nexp>, 46
List.replace <pointer_nexp>, <index_nexp>, <sexp>|<nexp>, 46
List.search <pointer_nexp>, value|value$, <result_nvar>{,<start_nexp>}, 47
List.size <pointer_nexp>, <nvar>, 47
List.toArray <pointer_nexp>, Array$[] | Array[], 47
List.type <pointer_nexp>, <svar>, 47
LOG(<nexp>), 59
LOG10(<nexp>), 59
LOWER$(<sexp>), 69
LowMemory.resume, 91
MAX(<nexp>, <nexp>), 57
MenuKey.resume, 91
MID$(<sexp>, <start_nexp>{, <count_nexp>}), 64
MIN(<nexp>, <nexp>), 57
mkdir <path_sexp>, 107
MOD(<nexp1>, <nexp2>), 58
MyPhoneNumber <svar>, 130
Notify <title_sexp>, <subtitle_sexp>, <alert_sexp>, <wait_lexp>, 147
OCT$(<nexp>), 69
OCT(<sexp>), 61
OnBackground:, 145
OnBackKey:, 90
OnBtReadReady:, 129
OnConsoleTouch:, 90
OnError:, 89
OnGrTouch:, 168
OnKbChange:, 103
OnKeyPress:, 91
OnLowMemory:, 91
OnMenuKey:, 90
OnTimer:, 133
Pause <ticks_nexp>, 148
Phone.call <sexp>, 130
Phone.dial <sexp>, 131
Phone.info <nexp>|<nvar>, 142
Phone.rcv.init, 131
Phone.rcv.next <state_nvar>, <number_svar>, 131
PI(), 59
Popup <message_sexp>{{, <x_nexp>}{, <y_nexp>}{, <duration_lexp>}}, 101
POW(<nexp1>, <nexp2>), 59
Print {<exp> {,|;}} ..., 96
Program.info <nexp>|<nvar>, 85
RANDOMIZE({<nexp>}), 57
Read.data <number>|<string> {,<number>|<string>...,<number>|<string>}, 91
Read.from <nexp>, 92
Read.next <var>, ..., 92
Rem, 52
REPLACE$( <sexp>, <argument_sexp>, <replace_sexp>), 65
RIGHT$(<sexp>, <count_nexp>), 65
Ringer.get.mode <nvar>, 135
Ringer.get.volume <vol_nvar> { , <max_nvar>, 135
Ringer.set.mode <nexp>, 135
Ringer.set.volume <nexp>, 135
RND(), 57
ROUND(<value_nexp>{, <count_nexp>{, <mode_sexp>}}), 58
Run <filename_sexp>{, <data_sexp>}, 85
Screen rotation, size[], realsize[], density, 143
Screen.rotation <nvar>, 144
Screen.size size[], realsize[], density, 144
Select <sel_nvar>, < Array$[]>|<list_nexp>, {,<title_sexp> {, <message_sexp> } } {,<press_lvar> }, 101
Sensors.close, 197
Sensors.list <sensor_array$[]>, 196
Sensors.open <type_nexp>{:<delay_nexp>}{, <type_nexp>{:<delay_nexp>}, ...}, 196
Sensors.read sensor_type, p1, p2, p3, 197
SGN(<nexp>), 56
SHIFT(<value_nexp>, <bits_nexp>), 61
SIN(<nexp>), 59
SINH(<nexp>), 59
Sms.rcv.init, 131
Sms.rcv.next <svar>, 131
Sms.send <number_sexp>, <message_sexp>, 131
Socket.client.close, 123
Socket.client.connect <server_sexp>, <port_nexp> { , <wait_lexp> }, 121
Socket.client.read.file <file_nexp>, 122
Socket.client.read.line <line_svar>, 122
Socket.client.read.ready <nvar>, 122
Socket.client.server.ip <svar>, 122
Socket.client.status <status_nvar>, 122
Socket.client.write.bytes <sexp>, 123
Socket.client.write.file <file_nexp>, 123
Socket.client.write.line <line_sexp>, 122
Socket.myIP <array$[]>{, <nvar>}, 123
Socket.myIP <svar>, 123
Socket.server.client.ip <nvar>, 125
Socket.server.close, 125
Socket.server.connect {<wait_lexp>}, 123
Socket.server.create <port_nexp>, 123
Socket.server.disconnect, 125
Socket.server.read.file <file_nexp>, 125
Socket.server.read.line <svar>, 124
Socket.server.read.ready <nvar>, 124
Socket.server.status <status_nvar>, 124
Socket.server.write.bytes <sexp>, 124
Socket.server.write.file <file_nexp>, 125
Socket.server.write.line <line_sexp>, 124
Soundpool.load <soundID_nvar>, <file_path_sexp>, 189
Soundpool.open <MaxStreams_nexp>, 189
Soundpool.pause <streamID_nexp>, 190
Soundpool.play <streamID_nvar>, <soundID_nexp>, <rightVolume_nexp>, <leftVolume_nexp>, <priority_nexp>, <loop_nexp>, <rate_nexp>, 189
Soundpool.release, 190
Soundpool.resume <streamID_nexp>, 190
Soundpool.setpriority <streamID_nexp>, <priority_nexp>, 190
Soundpool.setrate <streamID_nexp>, <rate_nexp>, 190
Soundpool.setvolume <streamID_nexp>, <leftVolume_nexp>, <rightVolume_nexp>, 190
Soundpool.stop <streamID_nexp>, 190
Soundpool.unload <soundID_nexp>, 189
Split <result_array$[]>, <sexp> {, <test_sexp>}, 136
Split.all <result_array$[]>, <sexp> {, <test_sexp>}, 136
Sql.close <DB_pointer_nvar>, 150
Sql.delete <DB_pointer_nvar>, <table_name_sexp>{,<where_sexp>{,<count_nvar>} }, 152
Sql.drop_table <DB_pointer_nvar>, <table_name_sexp>, 150
Sql.exec <DB_pointer_nvar>, <command_sexp>, 153
Sql.insert <DB_pointer_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$, ...,CN$, VN$, 150
Sql.new_table <DB_pointer_nvar>, <table_name_sexp>, C1$, C2$, ...,CN$, 150
Sql.next doneFlag, cursorVar {{, svar}...{, array$[]{, nColVar}}}, 152
Sql.open <DB_pointer_nvar>, <DB_name_sexp>, 150
Sql.query <cursor_nvar>, <DB_pointer_nvar>, <table_name_sexp>, <columns_sexp> {, <where_sexp> {, <order_sexp>} }, 151
Sql.query.length <length_nvar>, <cursor_nvar>, 151
Sql.query.position <position_nvar>, <cursor_nvar>, 151
Sql.raw_query <cursor_nvar>, <DB_pointer_nvar>, <query_sexp>, 153
Sql.update <DB_ptr_nvar>, <table_name_sexp>, C1$, V1$, C2$, V2$,...,CN$, VN${: <where_sexp>}, 153
SQR(<nexp>), 59
Stack.clear <ptr_nexp>, 51
Stack.create N|S, <ptr_nvar>, 51
Stack.isEmpty <ptr_nexp>, <nvar>, 51
Stack.peek <ptr_nexp>, <nvar>|<svar>, 51
Stack.pop <ptr_nexp>, <nvar>|<svar>, 51
Stack.push <ptr_nexp>, <nexp>|<sexp>, 51
Stack.type <ptr_nexp>, <svar>, 51
STARTS_WITH(<sub_sexp>, <base_sexp>{, <start_nexp>}, 62
STR$(<nexp>), 69
STT.listen, 138
STT.results <string_list_ptr_nexp>, 139
Su.close, 199
Su.open, 198
Su.read.line <svar>, 198
Su.read.ready <nvar>, 198
Su.write <sexp>, 198
Sw.begin <exp>, 87
Sw.break, 88
Sw.case <exp >, ..., 88
Sw.case <op><exp >, 88
Sw.default, 88
Sw.end, 88
Swap <nvar_a>|<svar_a>, <nvar_b>|<svar_b>, 148
System.close, 198
System.open, 198
System.read.line <svar>, 198
System.read.ready <nvar>, 198
System.write <sexp>, 198
TAN(<nexp>), 59
Text.close <file_table_nexp>, 109
Text.eof <file_table_nexp>, <lvar>, 110
Text.input <svar>{, { <text_sexp>} , <title_sexp> }, 101
Text.open {r|w|a}, <file_table_nvar>, <path_sexp>, 108
Text.position.get <file_table_nexp>, <position_nvar>, 110
Text.position.mark {{<file_table_nexp>}{, <marklimit_nexp>}}, 111
Text.position.set <file_table_nexp>, <position_nexp>, 110
Text.readln <file_table_nexp> {,<svar>}..., 109
Text.writeln <file_table_nexp>, {<exp> {,|;}} ..., 110
TGet <result_svar>, <prompt_sexp> {, <title_sexp>}, 102
Time {<time_nexp>,} Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST, 132
TIME(), 63
TIME(<year_exp>, <month_exp>, <day_exp>, <hour_exp>, <minute_exp>, <second_exp>), 63
Timer.clear, 134
Timer.resume, 133
Timer.set <interval_nexp>, 133
TimeZone.get <tz_svar>, 133
TimeZone.list <tz_list_pointer_nexp>, 133
TimeZone.set { <tz_sexp> }, 133
TODEGREES(<nexp>), 60
Tone <frequency_nexp>, <duration_nexp>{, <duration_chk_lexp}, 148
TORADIANS(<nexp>), 60
TRIM$(<sexp>{, <test_sexp>}), 65
TTS.init, 138
TTS.speak <sexp> {, <wait_lexp>}, 138
TTS.speak.toFile <sexp> {, <path_sexp>}, 138
TTS.stop, 138
UCODE(<sexp>{, <index_nexp>}), 61
UnDim Array[], Array$[], ..., 41
UPPER$(<sexp>), 69
USING$({<locale_sexp>} , <format_sexp> { , <exp>}...), 70
VAL(<sexp>), 60
VERSION$(), 69
Vibrate <pattern_array[{<start>,<length>}]>,<nexp>, 148
VolKeys.off, 149
VolKeys.on, 149
W_R.break, 81
W_R.continue, 81
WakeLock <code_nexp>{, <flags_nexp>, 145
While <lexp> / Repeat, 80
WiFi.info {{<SSID_svar>}{, <BSSID_svar>}{, <MAC_svar>}{, <IP_var>}{, <speed_nvar>}}, 144
WifiLock <code_nexp>, 146
WORD$(<source_sexp>, <n_nexp> {, <test_sexp>}), 66
Zip.close, <file_table_nexp>, 116
Zip.count <path_sexp>, <nvar}, 116
Zip.dir <path_sexp>, Array$[] {,<dirmark_sexp>}, 116
Zip.open {r|w|a}, <file_table_nvar>, <path_sexp>, 116
Zip.read <file_table_nexp> ,<buffer_svar>, <file_name_sexp>, 116, 117
