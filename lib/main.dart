import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(const NativeApp());
}

class NativeApp extends StatefulWidget {
  const NativeApp({Key? key}) : super(key: key);

  @override
  State<NativeApp> createState() => _NativeAppState();
}

class _NativeAppState extends State<NativeApp> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) async {
      PermissionStatus status = await Permission.camera.request();
      MethodChannel methodChannel = const MethodChannel("CameraService");
      methodChannel.invokeMethod("initialize");
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Stack(
          children: [
            AndroidView(
                  viewType: "<platform-view-type>",
                  layoutDirection: TextDirection.ltr,
                  creationParams: {},
                  creationParamsCodec: StandardMessageCodec(),
                ),

            Container(
              color: Colors.red,
              width: 500,
              height: 2000,
            )
          ],
        ),
      ),
    );
  }
}
