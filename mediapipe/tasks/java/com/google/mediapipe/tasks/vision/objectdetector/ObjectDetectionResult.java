// Copyright 2022 The MediaPipe Authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.mediapipe.tasks.vision.objectdetector;

import com.google.auto.value.AutoValue;
import com.google.mediapipe.tasks.core.TaskResult;
import com.google.mediapipe.formats.proto.DetectionProto.Detection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Represents the detection results generated by {@link ObjectDetector}. */
@AutoValue
public abstract class ObjectDetectionResult implements TaskResult {

  @Override
  public abstract long timestampMs();

  public abstract List<com.google.mediapipe.tasks.components.containers.Detection> detections();

  /**
   * Creates an {@link ObjectDetectionResult} instance from a list of {@link Detection} protobuf
   * messages.
   *
   * @param detectionList a list of {@link DetectionOuterClass.Detection} protobuf messages.
   * @param timestampMs a timestamp for this result.
   */
  public static ObjectDetectionResult create(List<Detection> detectionList, long timestampMs) {
    List<com.google.mediapipe.tasks.components.containers.Detection> detections = new ArrayList<>();
    for (Detection detectionProto : detectionList) {
      detections.add(
          com.google.mediapipe.tasks.components.containers.Detection.createFromProto(
              detectionProto));
    }
    return new AutoValue_ObjectDetectionResult(
        timestampMs, Collections.unmodifiableList(detections));
  }
}
