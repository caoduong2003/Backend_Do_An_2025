# Script debug API URLs
# Chạy: .\debug_api_urls.ps1

$baseUrl = "http://1.53.72.37:8080"
$results = @()

Write-Host "=== DEBUG API URLs ===" -ForegroundColor Green
Write-Host "Base URL: $baseUrl" -ForegroundColor Yellow
Write-Host ""

# Test các URL khác nhau cho chủ đề
$testUrls = @(
    @{URL="/api/chude"; Name="Chủ đề - Đúng"},
    @{URL="/api/chu/de"; Name="Chủ đề - Có thể sai"},
    @{URL="/api/chude/"; Name="Chủ đề - Có dấu /"},
    @{URL="/api/chuDe"; Name="Chủ đề - camelCase"},
    @{URL="/api/chu-de"; Name="Chủ đề - kebab-case"},
    @{URL="/api/tuvung"; Name="Từ vựng - Đúng"},
    @{URL="/api/tuvung/"; Name="Từ vựng - Có dấu /"},
    @{URL="/api/baigiang"; Name="Bài giảng - Đúng"},
    @{URL="/api/baigiang/"; Name="Bài giảng - Có dấu /"}
)

foreach ($test in $testUrls) {
    try {
        Write-Host "Testing: $($test.Name)" -ForegroundColor Cyan
        Write-Host "URL: $($test.URL)" -ForegroundColor Gray
        
        $response = Invoke-RestMethod -Uri "$baseUrl$($test.URL)" -Method "GET" -TimeoutSec 10
        $results += [PSCustomObject]@{URL="$($test.URL)"; Name="$($test.Name)"; Status="✅ SUCCESS"; Code="200"}
        Write-Host "✅ SUCCESS (200)" -ForegroundColor Green
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $errorMessage = $_.Exception.Message
        $results += [PSCustomObject]@{URL="$($test.URL)"; Name="$($test.Name)"; Status="❌ FAILED"; Code="$statusCode"; Error="$errorMessage"}
        Write-Host "❌ FAILED ($statusCode)" -ForegroundColor Red
        Write-Host "   Error: $errorMessage" -ForegroundColor Red
    }
    Write-Host ""
}

# Tổng kết
Write-Host "=== TỔNG KẾT ===" -ForegroundColor Green
$successCount = ($results | Where-Object {$_.Status -eq "✅ SUCCESS"}).Count
$failCount = ($results | Where-Object {$_.Status -eq "❌ FAILED"}).Count

Write-Host "✅ Thành công: $successCount" -ForegroundColor Green
Write-Host "❌ Thất bại: $failCount" -ForegroundColor Red

Write-Host "`n=== CHI TIẾT ===" -ForegroundColor Yellow
$results | Format-Table -AutoSize

# Lưu kết quả
$results | Export-Csv -Path "debug_api_urls_results.csv" -NoTypeInformation -Encoding UTF8
Write-Host "`nKết quả đã được lưu vào file: debug_api_urls_results.csv" -ForegroundColor Cyan 