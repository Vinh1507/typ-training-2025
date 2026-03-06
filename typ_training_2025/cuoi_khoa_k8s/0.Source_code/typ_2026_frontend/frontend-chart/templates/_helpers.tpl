{{/*
Expand the name of the chart.
*/}}
{{- define "frontend-chart.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "frontend-chart.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := include "frontend-chart.name" . -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "frontend-chart.labels" -}}
helm.sh/chart: {{ include "frontend-chart.chart" . }}
{{ include "frontend-chart.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{/*
Selector labels
*/}}
{{- define "frontend-chart.selectorLabels" -}}
app.kubernetes.io/name: {{ include "frontend-chart.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/*
Chart label
*/}}
{{- define "frontend-chart.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
ServiceAccount name
*/}}
{{- define "frontend-chart.serviceAccountName" -}}
{{- if .Values.serviceAccount.create -}}
{{- default (include "frontend-chart.fullname" .) .Values.serviceAccount.name -}}
{{- else -}}
{{- default "default" .Values.serviceAccount.name -}}
{{- end -}}
{{- end -}}

{{/*
Component helpers used by templates/*.yml
*/}}

{{- define "helm-chart.componentName" -}}
{{- $root := .root -}}
{{- $component := .component -}}
{{- /* Logic: Nếu tên Release (VD: typ-2026-frontend) đã chứa chữ "frontend" thì giữ nguyên */ -}}
{{- if contains $component $root.Release.Name -}}
{{- $root.Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- /* Ngược lại chưa có thì nối thêm vào */ -}}
{{- printf "%s-%s" $root.Release.Name $component | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}

{{- define "helm-chart.componentSelectorLabels" -}}
{{- $root := .root -}}
{{- $component := .component -}}
app.kubernetes.io/name: {{ $root.Chart.Name | quote }}
app.kubernetes.io/instance: {{ $root.Release.Name | quote }}
app.kubernetes.io/component: {{ $component | quote }}
{{- end -}}

{{- define "helm-chart.componentLabels" -}}
{{- $root := .root -}}
{{- $component := .component -}}
helm.sh/chart: {{ printf "%s-%s" $root.Chart.Name $root.Chart.Version | replace "+" "_" | quote }}
{{ include "helm-chart.componentSelectorLabels" (dict "root" $root "component" $component) }}
app.kubernetes.io/managed-by: {{ $root.Release.Service | quote }}
{{- end -}}
