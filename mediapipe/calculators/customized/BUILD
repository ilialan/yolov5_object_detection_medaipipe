# Copyright 2019 The MediaPipe Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

load("@bazel_skylib//lib:selects.bzl", "selects")
load("//mediapipe/framework/port:build_config.bzl", "mediapipe_proto_library")
load(
    "//mediapipe/framework/tool:mediapipe_graph.bzl",
    "mediapipe_binary_graph",
)
load("//mediapipe/framework:encode_binary_proto.bzl", "encode_binary_proto")
load("@org_tensorflow//tensorflow/lite/core/shims:cc_library_with_tflite.bzl", "cc_library_with_tflite")
load("//mediapipe/framework:more_selects.bzl", "more_selects")

licenses(["notice"])

package(default_visibility = ["//visibility:public"])

#exports_files(
#    glob(["testdata/image_to_tensor/*"]),
#    visibility = [
#        "//mediapipe/calculators/image:__subpackages__",
#        "//mediapipe/util:__subpackages__",
#    ],
#)

config_setting(
    name = "disable_gpu",
    define_values = {
        "MEDIAPIPE_DISABLE_GPU": "1",
    },
    visibility = ["//visibility:public"],
)

more_selects.config_setting_negation(
    name = "not_disable_gpu",
    negate = ":disable_gpu",
)

selects.config_setting_group(
    name = "platform_ios_with_gpu",
    match_all = [
        ":not_disable_gpu",
        "//mediapipe:ios",
    ],
)

selects.config_setting_group(
    name = "compute_shader_unavailable",
    match_any = [
        "//mediapipe/gpu:disable_gpu",
    ],
)


cc_library(
    name = "yolov5_tensors_to_detections_calculator",
    srcs = ["yolov5_tensors_to_detections_calculator.cc"],
    copts = select({
        "//mediapipe:apple": [
            "-x objective-c++",
            "-fobjc-arc",  # enable reference-counting
        ],
        "//conditions:default": [],
    }),
    features = ["-layering_check"],  # allow depending on tensors_to_detections_calculator_gpu_deps
    deps = [
        "//mediapipe/calculators/tensor:tensors_to_detections_calculator_cc_proto",
        "//mediapipe/framework:calculator_framework",
        "//mediapipe/framework:port",
        "//mediapipe/framework/api2:node",
        "//mediapipe/framework/deps:file_path",
        "//mediapipe/framework/formats:detection_cc_proto",
        "//mediapipe/framework/formats:location",
        "//mediapipe/framework/formats:tensor",
        "//mediapipe/framework/formats/object_detection:anchor_cc_proto",
        "//mediapipe/framework/port:ret_check",
        "@com_google_absl//absl/strings:str_format",
        "@com_google_absl//absl/types:span",
    ] + selects.with_or({
        ":compute_shader_unavailable": [],
        "//conditions:default": [":tensors_to_detections_calculator_gpu_deps"],
    }) + select({
        "//mediapipe:apple": [
            "//third_party/apple_frameworks:CoreVideo",
            "//third_party/apple_frameworks:MetalKit",
        ],
        "//conditions:default": [],
    }),
    alwayslink = 1,
)

cc_library(
    name = "tensors_to_detections_calculator_gpu_deps",
    visibility = ["//visibility:private"],
    deps = select({
        "//mediapipe:ios": [
            "//mediapipe/gpu:MPPMetalHelper",
            "//mediapipe/gpu:MPPMetalUtil",
        ],
        "//mediapipe:macos": [],
        "//conditions:default": [
            "//mediapipe/gpu:gl_calculator_helper",
        ],
    }),
)

