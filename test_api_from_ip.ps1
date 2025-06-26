# Script test API từ IP 1.53.72.37
# Chạy: .\test_api_from_ip.ps1

$baseUrl = "http://1.53.72.37:8080"
$results = @()

Write-Host "=== KIỂM TRA API TỪ IP 1.53.72.37 ===" -ForegroundColor Green
Write-Host "Base URL: $baseUrl" -ForegroundColor Yellow
Write-Host ""

# Test các API bị lỗi 403
$testEndpoints = @(
    @{Method="GET"; Path="/api/tuvung"; Name="Từ vựng - Danh sách"},
    @{Method="GET"; Path="/api/tuvung/1"; Name="Từ vựng - Chi tiết"},
    @{Method="GET"; Path="/api/chude"; Name="Chủ đề - Danh sách"},
    @{Method="GET"; Path="/api/chude/1"; Name="Chủ đề - Chi tiết"},
    @{Method="GET"; Path="/api/baigiang"; Name="Bài giảng - Danh sách"},
    @{Method="GET"; Path="/api/capdohsk"; Name="Cấp độ HSK - Danh sách"},
    @{Method="GET"; Path="/api/loaibaigiang"; Name="Loại bài giảng - Danh sách"},
    @{Method="GET"; Path="/api/tien-trinh"; Name="Tiến trình - Danh sách"},
    @{Method="GET"; Path="/api/auth/dangnhap"; Name="Auth - Test"}
)

foreach ($endpoint in $testEndpoints) {
    try {
        Write-Host "Testing: $($endpoint.Name)" -ForegroundColor Cyan
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.Path)" -Method $endpoint.Method -TimeoutSec 10
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Name="$($endpoint.Name)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ $($endpoint.Method) $($endpoint.Path) - SUCCESS" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $errorMessage = $_.Exception.Message
        $results += [PSCustomObject]@{Endpoint="$($endpoint.Method) $($endpoint.Path)"; Name="$($endpoint.Name)"; Status="❌ FAILED"; Code="$statusCode"; Error="$errorMessage"}
        Write-Host "❌ $($endpoint.Method) $($endpoint.Path) - FAILED ($statusCode)" -ForegroundColor Red
        Write-Host "   Error: $errorMessage" -ForegroundColor Red
    }
    Write-Host ""
}

# Tổng kết
Write-Host "=== TỔNG KẾT KẾT QUẢ ===" -ForegroundColor Green
$successCount = ($results | Where-Object {$_.Status -eq "✅ SUCCESS"}).Count
$failCount = ($results | Where-Object {$_.Status -eq "❌ FAILED"}).Count
$totalCount = $results.Count

Write-Host "Tổng số API tested: $totalCount" -ForegroundColor Yellow
Write-Host "✅ Thành công: $successCount" -ForegroundColor Green
Write-Host "❌ Thất bại: $failCount" -ForegroundColor Red

if ($failCount -gt 0) {
    Write-Host "`n=== CHI TIẾT LỖI ===" -ForegroundColor Red
    $results | Where-Object {$_.Status -eq "❌ FAILED"} | Format-Table -AutoSize
}

Write-Host "`n=== TẤT CẢ KẾT QUẢ ===" -ForegroundColor Yellow
$results | Format-Table -AutoSize

# Lưu kết quả vào file
$results | Export-Csv -Path "api_test_from_ip_results.csv" -NoTypeInformation -Encoding UTF8
Write-Host "`nKết quả đã được lưu vào file: api_test_from_ip_results.csv" -ForegroundColor Cyan 