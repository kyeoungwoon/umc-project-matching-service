'use client';

import { useRef, useState } from 'react';

import { FileIcon, Loader2, Upload, X } from 'lucide-react';
import { toast } from 'sonner';

import { Button } from '@styles/components/ui/button';
import { cn } from '@styles/lib/utils';

import { useUploadFile } from '@api/tanstack/s3.queries';

interface FileUploaderProps {
  /**
   * 파일 업로드 완료 시 호출되는 콜백
   * @param cdnUrl - 업로드된 파일의 CDN URL
   */
  onUploadComplete: (cdnUrl: string) => void;

  /**
   * 허용할 파일 타입 (예: "image/*", "image/png,image/jpeg", ".pdf,.doc")
   */
  accept?: string;

  /**
   * 최대 파일 크기 (바이트 단위, 기본값: 10MB)
   */
  maxSize?: number;

  /**
   * 버튼 텍스트
   */
  buttonText?: string;

  /**
   * 버튼 variant
   */
  variant?: 'default' | 'destructive' | 'outline' | 'secondary' | 'ghost' | 'link';

  /**
   * 버튼 크기
   */
  size?: 'default' | 'sm' | 'lg' | 'icon';

  /**
   * 업로드된 파일 미리보기 표시 여부 (이미지인 경우)
   */
  showPreview?: boolean;

  /**
   * 현재 업로드된 파일 URL (제어 컴포넌트로 사용 시)
   */
  value?: string;

  /**
   * 클래스명
   */
  className?: string;

  /**
   * 비활성화 여부
   */
  disabled?: boolean;
}

export function FileUploader({
  onUploadComplete,
  accept,
  maxSize = 10 * 1024 * 1024, // 10MB
  buttonText = '파일 업로드',
  variant = 'outline',
  size = 'default',
  showPreview = false,
  value,
  className,
  disabled = false,
}: FileUploaderProps) {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(value || null);
  const [fileName, setFileName] = useState<string | null>(null);

  const uploadFile = useUploadFile();

  const handleButtonClick = () => {
    if (disabled || uploadFile.isPending) return;
    fileInputRef.current?.click();
  };

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    // 파일 크기 검증
    if (file.size > maxSize) {
      toast.error('파일 크기 초과', {
        description: `파일 크기는 ${(maxSize / 1024 / 1024).toFixed(0)}MB를 초과할 수 없습니다.`,
      });
      return;
    }

    // 파일 타입 검증 (선택적)
    if (accept) {
      const acceptedTypes = accept.split(',').map((type) => type.trim());
      const isAccepted = acceptedTypes.some((acceptedType) => {
        if (acceptedType.startsWith('.')) {
          return file.name.toLowerCase().endsWith(acceptedType.toLowerCase());
        }
        if (acceptedType.endsWith('/*')) {
          const category = acceptedType.split('/')[0];
          return file.type.startsWith(category);
        }
        return file.type === acceptedType;
      });

      if (!isAccepted) {
        toast.error('지원하지 않는 파일 형식입니다.', {
          description: `허용된 형식: ${accept}`,
        });
        return;
      }
    }

    try {
      setFileName(file.name);

      // 이미지 미리보기 생성
      if (showPreview && file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onloadend = () => {
          setPreviewUrl(reader.result as string);
        };
        reader.readAsDataURL(file);
      }

      // 파일 업로드
      const result = await uploadFile.mutateAsync(file);

      setPreviewUrl(result.cdnUrl);
      toast.success('파일 업로드 성공!', {
        description: `${file.name}이(가) 업로드되었습니다.`,
      });

      onUploadComplete(result.cdnUrl);
    } catch (error) {
      console.error('파일 업로드 실패:', error);
      toast.error('파일 업로드 실패', {
        description: '파일을 업로드하는 중 오류가 발생했습니다.',
      });
      setPreviewUrl(null);
      setFileName(null);
    } finally {
      // input 초기화
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
    }
  };

  const handleRemove = (e: React.MouseEvent) => {
    e.stopPropagation();
    setPreviewUrl(null);
    setFileName(null);
    onUploadComplete('');
  };

  const isImage =
    previewUrl &&
    (previewUrl.match(/\.(jpg|jpeg|png|gif|webp)$/i) || previewUrl.startsWith('data:image'));

  return (
    <div className={cn('flex flex-col gap-2', className)}>
      <input
        ref={fileInputRef}
        type="file"
        accept={accept}
        onChange={handleFileChange}
        className="hidden"
        disabled={disabled || uploadFile.isPending}
      />

      <Button
        type="button"
        variant={variant}
        size={size}
        onClick={handleButtonClick}
        disabled={disabled || uploadFile.isPending}
        className={cn('relative')}
      >
        {uploadFile.isPending ? (
          <>
            <Loader2 className="h-4 w-4 animate-spin" />
            업로드 중...
          </>
        ) : (
          <>
            <Upload className="h-4 w-4" />
            {buttonText}
          </>
        )}
      </Button>

      {/* 미리보기 및 파일 정보 */}
      {showPreview && (previewUrl || fileName) && (
        <div className="border-border bg-muted/30 relative flex items-center gap-3 rounded-md border p-3">
          {isImage ? (
            <div className="relative h-20 w-20 overflow-hidden rounded-md">
              <img src={previewUrl || ''} alt="미리보기" className="h-full w-full object-cover" />
            </div>
          ) : (
            <div className="bg-muted flex h-20 w-20 items-center justify-center rounded-md">
              <FileIcon className="text-muted-foreground h-8 w-8" />
            </div>
          )}

          <div className="flex-1 overflow-hidden">
            <p className="truncate text-sm font-medium">{fileName || '파일'}</p>
            {previewUrl && (
              <a
                href={previewUrl}
                target="_blank"
                rel="noopener noreferrer"
                className="text-primary text-xs hover:underline"
              >
                파일 보기
              </a>
            )}
          </div>

          <Button
            type="button"
            variant="ghost"
            size="icon-sm"
            onClick={handleRemove}
            className="shrink-0"
          >
            <X className="h-4 w-4" />
          </Button>
        </div>
      )}

      {/* 업로드 진행 상태 */}
      {uploadFile.isPending && (
        <p className="text-muted-foreground text-xs">파일을 업로드하는 중입니다...</p>
      )}
    </div>
  );
}
