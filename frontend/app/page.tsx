"use client";

import React, { useState, useEffect, useRef } from "react";

// --- Clean SVG Icons (Zero Emojis) ---

function IconLogo() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1-2.5-2.5Z" />
      <path d="M6 6h10" />
      <path d="M6 10h10" />
    </svg>
  );
}

function IconChat() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M7.9 20A9 9 0 1 0 4 16.1L2 22Z" />
    </svg>
  );
}

function IconMaterials() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" />
      <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" />
    </svg>
  );
}

function IconFlashcards() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <rect width="18" height="14" x="3" y="5" rx="2" />
      <path d="M7 15h4" />
      <path d="M7 11h10" />
    </svg>
  );
}

function IconSun() {
  return (
    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="4" />
      <path d="M12 2v2" />
      <path d="M12 20v2" />
      <path d="m4.93 4.93 1.41 1.41" />
      <path d="m17.66 17.66 1.41 1.41" />
      <path d="M2 12h2" />
      <path d="M20 12h2" />
      <path d="m6.34 17.66-1.41 1.41" />
      <path d="m19.07 4.93-1.41 1.41" />
    </svg>
  );
}

function IconMoon() {
  return (
    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M12 3a6 6 0 0 0 9 9 9 9 0 1 1-9-9Z" />
    </svg>
  );
}

function IconUpload() {
  return (
    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
      <polyline points="17 8 12 3 7 8" />
      <line x1="12" x2="12" y1="3" y2="15" />
    </svg>
  );
}

function IconFile() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M15 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7Z" />
      <path d="M14 2v4a2 2 0 0 0 2 2h4" />
    </svg>
  );
}

function IconTrash() {
  return (
    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M3 6h18" />
      <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6" />
      <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2" />
    </svg>
  );
}

function IconArrowRight() {
  return (
    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M5 12h14" />
      <path d="m12 5 7 7-7 7" />
    </svg>
  );
}

function IconUser() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2" />
      <circle cx="12" cy="7" r="4" />
    </svg>
  );
}

function IconTutor() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="m12 3-1.9 5.8a2 2 0 0 1-1.3 1.3L3 12l5.8 1.9a2 2 0 0 1 1.3 1.3L12 21l1.9-5.8a2 2 0 0 1 1.3-1.3L21 12l-5.8-1.9a2 2 0 0 1-1.3-1.3Z" />
    </svg>
  );
}

function IconBook() {
  return (
    <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1-2.5-2.5Z" />
    </svg>
  );
}

function IconFlip() {
  return (
    <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="m3 16 4 4 4-4" />
      <path d="M7 20V4" />
      <path d="m21 8-4-4-4 4" />
      <path d="M17 4v16" />
    </svg>
  );
}

// --- Types ---

interface Message {
  id: string;
  sender: "user" | "tutor";
  text: string;
  sources?: string[];
  chunks?: any[];
  durationMs?: number;
  model?: string;
  timestamp: string;
}

interface DocItem {
  source: string;
  file_path: string;
  chunk_count: number;
  page_count: number;
  has_visuals?: boolean;
  visual_count?: number;
}

interface Flashcard {
  id: string;
  question: string;
  answer: string;
  category: string;
  difficulty: string;
  source: string;
}

interface SystemStatus {
  status: string;
  provider: string;
  nvidia_configured: boolean;
  nebius_configured: boolean;
  indexed_chunks: number;
  persist_directory: string;
  generation_model: string;
}

// --- Gemini-Style Formatted Message Renderer ---

function cleanHeadingText(text: string): string {
  let cleaned = text.trim();
  cleaned = cleaned.replace(/^#+\s*/, "");
  for (let i = 0; i < 3; i++) {
    cleaned = cleaned.trim();
    cleaned = cleaned.replace(/^["'“”«»]+|["'“”«»]+$/g, "");
    cleaned = cleaned.replace(/^\*\*+|\*\*+$/g, "");
    cleaned = cleaned.replace(/^__+|__+$/g, "");
    cleaned = cleaned.replace(/^\*+|\*+$/g, "");
  }
  return cleaned.trim();
}

function getCitationKey(cit: string): string {
  const m = cit.match(/Source:\s*([^,|\])]+)(?:[,|]\s*(?:Page:?|p\.?)\s*([^\])]+))?/i);
  if (m) {
    const file = m[1].trim().toLowerCase();
    const page = m[2] ? m[2].trim() : "";
    return `${file}#${page}`;
  }
  return cit.trim().toLowerCase();
}

/**
 * Automatically relocates any citations trapped inside the middle of a sentence
 * or bullet item to the very end of that sentence/bullet, exactly like Gemini web.
 */
function moveCitationsToEndOfSentences(line: string): string {
  const citRegex = /(\[(?:Source:|Document\s*\d+:?)[^\]]+\]|\(Source:[^)]+\))/gi;
  if (!citRegex.test(line)) return line;

  const isBullet = /^[*•-]\s+|^\d+\.\s+/.test(line);

  if (isBullet) {
    const citations: string[] = [];
    const cleaned = line
      .replace(citRegex, (match) => {
        citations.push(match);
        return "";
      })
      .replace(/\s{2,}/g, " ")
      .replace(/\s+([.,;:])/g, "$1")
      .trim();

    if (citations.length === 0) return line;
    return `${cleaned} ${citations.join(" ")}`;
  }

  // Paragraph sentence splitter without regex lookbehind dependency
  const rawSentences = line.split(/([.?!]\s+)/);
  const fullSentences: string[] = [];
  for (let s = 0; s < rawSentences.length; s += 2) {
    const sentText = rawSentences[s];
    const sentDelim = rawSentences[s + 1] || "";
    if (sentText || sentDelim) {
      fullSentences.push(sentText + sentDelim);
    }
  }

  const processed = fullSentences.map((sentence) => {
    const citations: string[] = [];
    const cleaned = sentence
      .replace(citRegex, (match) => {
        citations.push(match);
        return "";
      })
      .replace(/\s{2,}/g, " ")
      .replace(/\s+([.,;:])/g, "$1")
      .trim();

    if (citations.length === 0) return sentence;
    return `${cleaned} ${citations.join(" ")}`;
  });

  return processed.join(" ");
}

/**
 * Gemini-Style Citation Chip: Compact numbered pill in-line,
 * with an interactive preview card on hover and click-to-view modal.
 */
function GeminiCitationChip({
  citationNumber,
  citationStr,
  chunk,
  onClick,
}: {
  citationNumber: number;
  citationStr: string;
  chunk?: any;
  onClick: () => void;
}) {
  const [isHovered, setIsHovered] = useState(false);

  const match = citationStr.match(/Source:\s*([^,|\])]+)(?:[,|]\s*(?:Page:?|p\.?)\s*([^\])]+))?/i);
  const fileName = chunk?.source || (match ? match[1].trim() : "Course Material");
  const pageNum = chunk?.page || (match && match[2] ? match[2].trim() : null);
  const previewText =
    chunk?.preview || chunk?.text || "Verified academic source document excerpt.";

  return (
    <span
      className="gemini-citation-wrapper"
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <button
        type="button"
        className="gemini-citation-chip"
        onClick={(e) => {
          e.stopPropagation();
          onClick();
        }}
        aria-label={`Source ${citationNumber}: ${fileName}`}
      >
        {citationNumber}
      </button>

      {isHovered && (
        <span className="gemini-hover-card" onClick={(e) => e.stopPropagation()}>
          <span className="hover-card-header">
            <span className="hover-card-file">
              <IconBook />
              <span title={fileName}>{fileName}</span>
            </span>
            {pageNum && <span className="hover-card-page">p. {pageNum}</span>}
          </span>
          <span className="hover-card-preview">{previewText}</span>
          <span className="hover-card-footer" onClick={onClick}>
            Click to inspect source &rarr;
          </span>
        </span>
      )}
    </span>
  );
}

function getCalloutIcon(type: string): string {
  switch (type.toUpperCase()) {
    case "SUMMARY":
    case "ABSTRACT":
    case "TLDR":
      return "📋";
    case "NOTE":
      return "ℹ️";
    case "TIP":
    case "HINT":
    case "IMPORTANT":
      return "💡";
    case "WARNING":
    case "CAUTION":
    case "ATTENTION":
      return "⚠️";
    case "INFO":
      return "ℹ️";
    case "DANGER":
    case "ERROR":
    case "FAILURE":
      return "🛑";
    default:
      return "📝";
  }
}

function renderInlineTokens(
  text: string,
  citationMap: Map<string, number>,
  chunks?: any[],
  onCitationClick?: (source: string, chunk?: any) => void
): React.ReactNode[] {
  const tokenRegex = /(\[(?:Source:)[^\]]+\]|\(Source:[^)]+\)|\[\[[^\]]+\]\]|#[a-zA-Z0-9_\/-]+|\*\*[^*]+\*\*|`[^`]+`|\*[^*]+\*)/gi;
  const parts = text.split(tokenRegex);

  return parts.map((part, idx) => {
    if (!part) return null;

    // 1. Inline Citation Badge [Source: ...] or (Source: ...) -> Gemini numbered pill
    const isBracketCit = part.startsWith("[Source:") && part.endsWith("]");
    const isParenCit = part.startsWith("(Source:") && part.endsWith(")");
    if (isBracketCit || isParenCit) {
      const citKey = getCitationKey(part);
      const citNum = citationMap.get(citKey) || 1;

      // Match chunk
      const m = part.match(/Source:\s*([^,|\])]+)(?:[,|]\s*(?:Page:?|p\.?)\s*([^\])]+))?/i);
      const fileName = m ? m[1].trim() : "";
      const pageNum = m && m[2] ? m[2].trim() : "";

      const matchedChunk =
        chunks?.find((c) => {
          const src = (c.source || c.metadata?.source || "").toLowerCase();
          const p = String(c.page || c.metadata?.page || "");
          const fileMatches = fileName && src.includes(fileName.toLowerCase());
          const pageMatches = !pageNum || p === pageNum;
          return fileMatches && pageMatches;
        }) ||
        chunks?.find((c) => {
          const src = (c.source || c.metadata?.source || "").toLowerCase();
          return fileName && src.includes(fileName.toLowerCase());
        });

      return (
        <GeminiCitationChip
          key={`cit-${idx}`}
          citationNumber={citNum}
          citationStr={part}
          chunk={matchedChunk}
          onClick={() => onCitationClick && onCitationClick(part, matchedChunk)}
        />
      );
    }

    // 2. Obsidian Wikilinks: [[Note Name]] or [[Folder/Note Name|Alias]]
    if (part.startsWith("[[") && part.endsWith("]]") && part.length >= 4) {
      const inner = part.slice(2, -2).trim();
      const pipeParts = inner.split("|");
      const target = pipeParts[0].trim();
      const label = pipeParts.length > 1 ? pipeParts[1].trim() : target;
      return (
        <span
          key={`wiki-${idx}`}
          className="obsidian-wikilink"
          title={`Obsidian Note: ${target}`}
        >
          [[{label}]]
        </span>
      );
    }

    // 3. Obsidian Nested Tags: #topic/subtopic
    if (part.startsWith("#") && !part.startsWith("##") && part.length >= 2 && /^#[a-zA-Z0-9_\/-]+$/.test(part)) {
      return (
        <span key={`tag-${idx}`} className="obsidian-tag">
          {part}
        </span>
      );
    }

    // 4. Bold (strip decorative quotes inside bold like **"Logic"**)
    if (part.startsWith("**") && part.endsWith("**") && part.length >= 4) {
      let boldContent = part.slice(2, -2).trim();
      boldContent = boldContent.replace(/^["'“”«»]+|["'“”«»]+$/g, "");
      return <strong key={`b-${idx}`}>{boldContent}</strong>;
    }

    // 5. Inline Code
    if (part.startsWith("`") && part.endsWith("`") && part.length >= 2) {
      return <code key={`code-${idx}`}>{part.slice(1, -1)}</code>;
    }

    // 6. Italic
    if (part.startsWith("*") && part.endsWith("*") && part.length >= 2) {
      return <em key={`em-${idx}`}>{part.slice(1, -1)}</em>;
    }

    // 7. Plain text
    return <React.Fragment key={`txt-${idx}`}>{part}</React.Fragment>;
  });
}

function FormattedMessage({
  text,
  chunks,
  onCitationClick,
}: {
  text: string;
  chunks?: any[];
  onCitationClick?: (source: string, chunk?: any) => void;
}) {
  if (!text) return null;

  // Build unique citation map for this message
  const citationMap = new Map<string, number>();
  const citMatches = text.match(/\[(?:Source:)[^\]]+\]|\(Source:[^)]+\)/gi) || [];
  let nextCitNum = 1;
  citMatches.forEach((m) => {
    const key = getCitationKey(m);
    if (!citationMap.has(key)) {
      citationMap.set(key, nextCitNum++);
    }
  });

  // Check for YAML Frontmatter at top
  let frontmatterNode: React.ReactNode = null;
  let textToParse = text;

  const frontmatterMatch = text.match(/^---\s*[\r\n]+([\s\S]*?)[\r\n]+---\s*[\r\n]*/);
  if (frontmatterMatch) {
    const yaml = frontmatterMatch[1];
    textToParse = text.slice(frontmatterMatch[0].length);

    const titleMatch = yaml.match(/title:\s*["']?([^"'\r\n]+)["']?/i);
    const dateMatch = yaml.match(/date:\s*([^\r\n]+)/i);
    const createdByMatch = yaml.match(/created_by:\s*([^\r\n]+)/i);

    const tags: string[] = [];
    const tagSection = yaml.match(/tags:[\s\S]*?(?=(?:[a-zA-Z0-9_]+:|$))/i);
    if (tagSection) {
      const tagLines = tagSection[0].match(/-[ \t]+([^\r\n]+)/g);
      if (tagLines) {
        tagLines.forEach((t) => tags.push(t.replace(/^-[ \t]+/, "").trim()));
      }
    }

    frontmatterNode = (
      <div className="obsidian-frontmatter-card" key="frontmatter">
        <div className="frontmatter-top-bar">
          <div className="frontmatter-title-group">
            <span className="frontmatter-file-badge">Obsidian Note</span>
            <h3 className="frontmatter-note-title">
              {titleMatch ? titleMatch[1].trim() : "Study Note"}
            </h3>
          </div>
          {createdByMatch && (
            <span className="frontmatter-author-pill">
              by <strong>{createdByMatch[1].trim()}</strong>
            </span>
          )}
        </div>
        <div className="frontmatter-meta-row">
          {dateMatch && (
            <span className="frontmatter-date-badge">📅 {dateMatch[1].trim()}</span>
          )}
          {tags.length > 0 && (
            <div className="frontmatter-tags-list">
              {tags.map((tg, i) => (
                <span key={i} className="obsidian-tag-pill">
                  #{tg.replace(/^#/, "")}
                </span>
              ))}
            </div>
          )}
        </div>
      </div>
    );
  }

  const lines = textToParse.split("\n");
  const nodes: React.ReactNode[] = [];
  let currentList: { type: "ul" | "ol"; items: string[] } | null = null;
  let paragraphBuffer: string[] = [];
  let inCodeBlock = false;
  let codeBuffer: string[] = [];

  const flushParagraph = () => {
    if (paragraphBuffer.length > 0) {
      const pText = paragraphBuffer.join(" ").trim();
      if (pText) {
        nodes.push(
          <p key={`p-${nodes.length}`}>
            {renderInlineTokens(pText, citationMap, chunks, onCitationClick)}
          </p>
        );
      }
      paragraphBuffer = [];
    }
  };

  const flushList = () => {
    if (currentList) {
      const ListTag = currentList.type;
      const listItems = currentList.items;
      nodes.push(
        <ListTag key={`list-${nodes.length}`}>
          {listItems.map((item, idx) => (
            <li key={idx}>
              {renderInlineTokens(item, citationMap, chunks, onCitationClick)}
            </li>
          ))}
        </ListTag>
      );
      currentList = null;
    }
  };

  const flushAll = () => {
    flushParagraph();
    flushList();
  };

  for (let i = 0; i < lines.length; i++) {
    const rawLine = lines[i];
    const trimmed = rawLine.trim();

    // Code block fences
    if (trimmed.startsWith("```")) {
      if (inCodeBlock) {
        nodes.push(
          <pre key={`pre-${nodes.length}`}>
            <code>{codeBuffer.join("\n")}</code>
          </pre>
        );
        codeBuffer = [];
        inCodeBlock = false;
      } else {
        flushAll();
        inCodeBlock = true;
      }
      continue;
    }

    if (inCodeBlock) {
      codeBuffer.push(rawLine);
      continue;
    }

    // Move any mid-sentence citations cleanly to the end of the sentence or bullet
    const normalizedLine = moveCitationsToEndOfSentences(trimmed);

    // Check for Obsidian Callouts: > [!TYPE] Optional Title
    const calloutMatch = normalizedLine.match(/^>\s*\[!([a-zA-Z0-9_-]+)\][ \t]*(.*)$/);
    if (calloutMatch) {
      flushAll();
      const calloutType = calloutMatch[1].toUpperCase();
      const calloutTitle = calloutMatch[2].trim() || calloutType;
      const calloutBodyLines: string[] = [];

      // Collect subsequent lines starting with >
      while (i + 1 < lines.length && lines[i + 1].trim().startsWith(">")) {
        i++;
        const nextBodyLine = lines[i].trim().replace(/^>\s?/, "");
        calloutBodyLines.push(nextBodyLine);
      }

      nodes.push(
        <div
          key={`callout-${nodes.length}`}
          className={`obsidian-callout callout-${calloutType.toLowerCase()}`}
        >
          <div className="callout-header">
            <span className="callout-icon">{getCalloutIcon(calloutType)}</span>
            <span className="callout-title-text">{calloutTitle}</span>
          </div>
          <div className="callout-body">
            {calloutBodyLines.length > 0 ? (
              calloutBodyLines.map((bLine, bIdx) => (
                <p key={bIdx}>
                  {renderInlineTokens(bLine, citationMap, chunks, onCitationClick)}
                </p>
              ))
            ) : (
              <p>{calloutTitle}</p>
            )}
          </div>
        </div>
      );
      continue;
    }

    // Check Setext Underlines on the NEXT line (=== or ---)
    const nextLine = i + 1 < lines.length ? lines[i + 1].trim() : "";
    const isSetextH1 = /^={3,}$/.test(nextLine);
    const isSetextH2 = /^-{3,}$/.test(nextLine);

    if (normalizedLine && (isSetextH1 || isSetextH2)) {
      flushAll();
      const cleanHeader = cleanHeadingText(normalizedLine);
      if (isSetextH1) {
        nodes.push(<h2 key={`h2-${nodes.length}`}>{cleanHeader}</h2>);
      } else {
        nodes.push(<h3 key={`h3-${nodes.length}`}>{cleanHeader}</h3>);
      }
      i++; // Skip underline line
      continue;
    }

    // Solitary horizontal separator lines (====, ----, ____, ****)
    if (/^([=\-_*])\1{2,}$/.test(normalizedLine)) {
      flushAll();
      nodes.push(<hr key={`hr-${nodes.length}`} />);
      continue;
    }

    // Empty lines flush paragraphs and lists
    if (!normalizedLine) {
      flushAll();
      continue;
    }

    // ATX Headers (#, ##, ###, ####)
    const headerMatch = normalizedLine.match(/^(#{1,6})\s+(.*)$/);
    if (headerMatch) {
      flushAll();
      const level = headerMatch[1].length;
      const cleanTitle = cleanHeadingText(headerMatch[2]);
      if (level <= 2) {
        nodes.push(<h2 key={`h-${nodes.length}`}>{cleanTitle}</h2>);
      } else if (level === 3) {
        nodes.push(<h3 key={`h-${nodes.length}`}>{cleanTitle}</h3>);
      } else {
        nodes.push(<h4 key={`h-${nodes.length}`}>{cleanTitle}</h4>);
      }
      continue;
    }

    // Unordered list items (*, -, •)
    const bulletMatch = normalizedLine.match(/^[*•-]\s+(.*)$/);
    if (bulletMatch) {
      flushParagraph();
      if (!currentList || currentList.type !== "ul") {
        flushList();
        currentList = { type: "ul", items: [] };
      }
      currentList.items.push(bulletMatch[1]);
      continue;
    }

    // Ordered list items (1., 2., etc.)
    const orderedMatch = normalizedLine.match(/^\d+\.\s+(.*)$/);
    if (orderedMatch) {
      flushParagraph();
      if (!currentList || currentList.type !== "ol") {
        flushList();
        currentList = { type: "ol", items: [] };
      }
      currentList.items.push(orderedMatch[1]);
      continue;
    }

    // Regular text line -> buffer for paragraph
    flushList();
    paragraphBuffer.push(normalizedLine);
  }

  // Handle unclosed code block if streaming
  if (inCodeBlock && codeBuffer.length > 0) {
    nodes.push(
      <pre key={`pre-${nodes.length}`}>
        <code>{codeBuffer.join("\n")}</code>
      </pre>
    );
  }

  flushAll();

  return (
    <>
      {frontmatterNode}
      {nodes}
    </>
  );
}

export default function StudyAssistantApp() {
  // Navigation tabs (RAG pipeline tab removed per requirement)
  const [activeTab, setActiveTab] = useState<"chat" | "materials" | "flashcards">("chat");

  // Theme state: dark / light
  const [theme, setTheme] = useState<"dark" | "light">("dark");

  // Status state
  const [status, setStatus] = useState<SystemStatus>({
    status: "connecting",
    provider: "NVIDIA NIM",
    nvidia_configured: true,
    nebius_configured: true,
    indexed_chunks: 0,
    persist_directory: "./chroma_db",
    generation_model: "nvidia/Llama-3.1-Nemotron-70B-Instruct-HF",
  });

  // Chat state
  const [messages, setMessages] = useState<Message[]>([
    {
      id: "welcome",
      sender: "tutor",
      text: "Welcome to Study Assistant. Ask any question regarding your course materials to receive grounded explanations with verified page citations.\n\nTip: You can upload your syllabus, notes, or sample Kepler materials in the Course Materials tab.",
      sources: [],
      timestamp: "Just now",
    },
  ]);
  const [inputQuery, setInputQuery] = useState("");
  const [isGenerating, setIsGenerating] = useState(false);
  const [augmentMode, setAugmentMode] = useState<"expand" | "rewrite" | "hyde">("expand");
  const [useWeb, setUseWeb] = useState(false);
  const [selectedCitation, setSelectedCitation] = useState<any | null>(null);
  const [savedObsidianIds, setSavedObsidianIds] = useState<{ [id: string]: boolean }>({});
  const [copiedNoteIds, setCopiedNoteIds] = useState<{ [id: string]: boolean }>({});

  // Retrieval configuration
  const topK = 15;
  const topN = 5;

  // Save note directly to local Obsidian Vault
  const handleSaveToObsidian = async (msg: Message) => {
    try {
      const titleMatch = msg.text.match(/title:\s*["']?([^"'\r\n]+)["']?/i);
      const title = titleMatch ? titleMatch[1].trim() : "Study Note";

      const res = await fetch("/api/obsidian/save", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          title,
          content: msg.text,
          folder: "Study Notes",
          tags: ["study/notes", "research"],
        }),
      });
      const data = await res.json();
      if (data.success) {
        setSavedObsidianIds((prev) => ({ ...prev, [msg.id]: true }));
        setTimeout(() => {
          setSavedObsidianIds((prev) => ({ ...prev, [msg.id]: false }));
        }, 4000);
      }
    } catch (err) {
      console.error("Failed to save to Obsidian Vault:", err);
    }
  };

  // Materials state
  const [documents, setDocuments] = useState<DocItem[]>([]);
  const [isUploading, setIsUploading] = useState(false);
  const [uploadMessage, setUploadMessage] = useState<{ type: "success" | "error" | "info"; text: string } | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

  // Flashcards state
  const [flashcards, setFlashcards] = useState<Flashcard[]>([]);
  const [flippedCards, setFlippedCards] = useState<{ [id: string]: boolean }>({});
  const [cardScores, setCardScores] = useState<{ [id: string]: "review" | "mastered" }>({});
  const [isGeneratingCards, setIsGeneratingCards] = useState(false);
  const [flashcardTopic, setFlashcardTopic] = useState("");

  const chatEndRef = useRef<HTMLDivElement>(null);

  // Initialize theme from localStorage or system preference
  useEffect(() => {
    const savedTheme = localStorage.getItem("study-assistant-theme") as "dark" | "light" | null;
    if (savedTheme) {
      setTheme(savedTheme);
      document.documentElement.setAttribute("data-theme", savedTheme);
    } else {
      document.documentElement.setAttribute("data-theme", "dark");
    }
    fetchStatus();
    fetchDocuments();
  }, []);

  // Theme toggle handler
  const toggleTheme = () => {
    const nextTheme = theme === "dark" ? "light" : "dark";
    setTheme(nextTheme);
    document.documentElement.setAttribute("data-theme", nextTheme);
    localStorage.setItem("study-assistant-theme", nextTheme);
  };

  useEffect(() => {
    if (activeTab === "chat") {
      chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages, activeTab]);

  const fetchStatus = async () => {
    try {
      const res = await fetch("/api/status");
      if (res.ok) {
        const data = await res.json();
        setStatus(data);
      }
    } catch {
      // Backend offline or starting
    }
  };

  const fetchDocuments = async () => {
    try {
      const res = await fetch("/api/documents");
      if (res.ok) {
        const data = await res.json();
        setDocuments(data.documents || []);
      }
    } catch {
      // Ignore
    }
  };

  // Submit academic question via SSE streaming
  const handleAsk = async (queryText?: string) => {
    const q = queryText || inputQuery;
    if (!q.trim() || isGenerating) return;

    setInputQuery("");
    const userMsgId = `user-${Date.now()}`;
    const tutorMsgId = `tutor-${Date.now()}`;

    const userMsg: Message = {
      id: userMsgId,
      sender: "user",
      text: q.trim(),
      timestamp: new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
    };

    const initialTutorMsg: Message = {
      id: tutorMsgId,
      sender: "tutor",
      text: "",
      sources: [],
      timestamp: new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
    };

    setMessages((prev) => [...prev, userMsg, initialTutorMsg]);
    setIsGenerating(true);

    const startTime = Date.now();

    try {
      const url = `/api/ask/stream?query=${encodeURIComponent(q)}&top_k=${topK}&top_n=${topN}&augment_mode=${augmentMode}&use_web=${useWeb}`;
      const response = await fetch(url);

      if (!response.ok || !response.body) {
        throw new Error("Streaming connection failed.");
      }

      const reader = response.body.getReader();
      const decoder = new TextDecoder();
      let streamedAnswer = "";
      let finalSources: string[] = [];
      let finalChunks: any[] = [];

      while (true) {
        const { value, done } = await reader.read();
        if (done) break;

        const chunkText = decoder.decode(value);
        const lines = chunkText.split("\n\n");

        for (const line of lines) {
          if (line.startsWith("data: ")) {
            try {
              const event = JSON.parse(line.replace("data: ", ""));
              if (event.type === "token") {
                streamedAnswer += event.token;
                setMessages((prev) =>
                  prev.map((msg) =>
                    msg.id === tutorMsgId ? { ...msg, text: streamedAnswer } : msg
                  )
                );
              } else if (event.type === "chunks") {
                finalChunks = event.chunks || [];
              } else if (event.type === "done") {
                finalSources = event.sources || [];
              }
            } catch {
              // Ignore partial frames
            }
          }
        }
      }

      const durationMs = Date.now() - startTime;
      setMessages((prev) =>
        prev.map((msg) =>
          msg.id === tutorMsgId
            ? {
                ...msg,
                text: streamedAnswer || "No response received.",
                sources: finalSources,
                chunks: finalChunks,
                durationMs,
              }
            : msg
        )
      );
    } catch {
      // Fallback: Non-streaming POST
      try {
        const res = await fetch("/api/ask", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            query: q,
            top_k: topK,
            top_n: topN,
            augment_mode: augmentMode,
            use_web: useWeb,
          }),
        });
        const data = await res.json();
        setMessages((prev) =>
          prev.map((msg) =>
            msg.id === tutorMsgId
              ? {
                  ...msg,
                  text: data.answer || "Could not retrieve answer.",
                  sources: data.sources || [],
                  chunks: data.chunks || [],
                  durationMs: data.duration_ms,
                  model: data.model,
                }
              : msg
          )
        );
      } catch (postErr: any) {
        setMessages((prev) =>
          prev.map((msg) =>
            msg.id === tutorMsgId
              ? {
                  ...msg,
                  text: `Connection Error: Unable to communicate with the Study Assistant engine on port 8000. (${postErr.message})`,
                }
              : msg
          )
        );
      }
    } finally {
      setIsGenerating(false);
      fetchStatus();
    }
  };

  // Upload file
  const handleFileUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setIsUploading(true);
    setUploadMessage({
      type: "info",
      text: `Uploading '${file.name}' to server...`,
    });

    const formData = new FormData();
    formData.append("file", file);

    try {
      const res = await fetch("/api/ingest/file?background=true", {
        method: "POST",
        body: formData,
      });

      const contentType = res.headers.get("content-type") || "";
      let data: any = {};
      if (contentType.includes("application/json")) {
        data = await res.json();
      } else {
        const rawText = await res.text();
        throw new Error(rawText || `Server returned status ${res.status}`);
      }

      if (!res.ok) {
        throw new Error(data.detail || `Upload failed with status ${res.status}`);
      }

      if (data.job_id) {
        let isDone = false;
        let pollCount = 0;
        const maxPolls = 600; // 10 minutes timeout at 1s intervals

        while (!isDone && pollCount < maxPolls) {
          await new Promise((resolve) => setTimeout(resolve, 1000));
          pollCount++;

          try {
            const statusRes = await fetch(`/api/ingest/status/${data.job_id}`);
            const statusContentType = statusRes.headers.get("content-type") || "";
            if (statusRes.ok && statusContentType.includes("application/json")) {
              const job = await statusRes.json();
              if (job.status === "processing") {
                const progressInfo = job.total_chunks > 0
                  ? `${job.progress}% (${job.processed_chunks}/${job.total_chunks} chunks)`
                  : "extracting pages...";
                setUploadMessage({
                  type: "info",
                  text: `Ingesting '${file.name}' (${progressInfo})...`,
                });
              } else if (job.status === "completed") {
                isDone = true;
                const count = job.result?.chunks_count ?? job.total_chunks ?? "all";
                setUploadMessage({
                  type: "success",
                  text: `Successfully ingested '${file.name}' (${count} chunks indexed)`,
                });
                fetchDocuments();
                fetchStatus();
                break;
              } else if (job.status === "failed") {
                isDone = true;
                throw new Error(job.error || "Background ingestion failed.");
              }
            }
          } catch (pollErr: any) {
            if (pollErr.message && !pollErr.message.includes("fetch")) {
              throw pollErr;
            }
          }
        }
      } else {
        const count = data.result?.chunks_count ?? data.total_indexed_chunks ?? "all";
        setUploadMessage({
          type: "success",
          text: `Successfully ingested '${file.name}' (${count} chunks indexed)`,
        });
        fetchDocuments();
        fetchStatus();
      }
    } catch (err: any) {
      setUploadMessage({
        type: "error",
        text: `Ingestion error: ${err.message}`,
      });
    } finally {
      setIsUploading(false);
      if (fileInputRef.current) fileInputRef.current.value = "";
    }
  };

  // Ingest sample Kepler astronomy document
  const handleIngestSample = async () => {
    setIsUploading(true);
    setUploadMessage({
      type: "info",
      text: "Ingesting Kepler's Planetary Laws sample material...",
    });
    try {
      const res = await fetch("/api/ingest/sample", { method: "POST" });
      const contentType = res.headers.get("content-type") || "";
      let data: any = {};
      if (contentType.includes("application/json")) {
        data = await res.json();
      } else {
        const text = await res.text();
        throw new Error(text || `Server returned status ${res.status}`);
      }

      if (res.ok) {
        setUploadMessage({
          type: "success",
          text: "Kepler's Planetary Laws sample material indexed successfully.",
        });
        fetchDocuments();
        fetchStatus();
      } else {
        setUploadMessage({
          type: "error",
          text: `Error: ${data.detail || "Could not load sample"}`,
        });
      }
    } catch (err: any) {
      setUploadMessage({
        type: "error",
        text: `Sample ingestion failed: ${err.message}`,
      });
    } finally {
      setIsUploading(false);
    }
  };

  // Clear database
  const handleClearDatabase = async () => {
    if (!confirm("Are you sure you want to clear all indexed documents from ChromaDB?")) return;
    try {
      const res = await fetch("/api/clear", { method: "POST" });
      if (res.ok) {
        fetchDocuments();
        fetchStatus();
      }
    } catch (err: any) {
      alert(`Clear failed: ${err.message}`);
    }
  };

  // Generate flashcards
  const handleGenerateFlashcards = async () => {
    setIsGeneratingCards(true);
    try {
      const res = await fetch("/api/flashcards", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          topic: flashcardTopic.trim() || undefined,
          count: 4,
        }),
      });
      const data = await res.json();
      if (res.ok && data.flashcards) {
        setFlashcards(data.flashcards);
        setFlippedCards({});
      } else {
        alert(data.detail || "Unable to generate flashcards. Please ingest documents first.");
      }
    } catch (err: any) {
      alert(`Failed to generate flashcards: ${err.message}`);
    } finally {
      setIsGeneratingCards(false);
    }
  };

  const toggleCardFlip = (id: string) => {
    setFlippedCards((prev) => ({ ...prev, [id]: !prev[id] }));
  };

  const scoreCard = (id: string, score: "review" | "mastered", e: React.MouseEvent) => {
    e.stopPropagation();
    setCardScores((prev) => ({ ...prev, [id]: score }));
  };

  return (
    <div className="app-shell">
      {/* Sidebar Navigation */}
      <aside className="app-sidebar">
        <div className="sidebar-header">
          <div className="brand-badge">
            <IconLogo />
          </div>
          <div className="brand-info">
            <h1>Study Assistant</h1>
            <span>Academic RAG Tutor</span>
          </div>
        </div>

        <nav className="sidebar-nav">
          <button
            className={`nav-item ${activeTab === "chat" ? "active" : ""}`}
            onClick={() => setActiveTab("chat")}
            id="nav-chat"
          >
            <span className="nav-icon">
              <IconChat />
            </span>
            <span className="nav-label">Academic Tutor</span>
          </button>

          <button
            className={`nav-item ${activeTab === "materials" ? "active" : ""}`}
            onClick={() => setActiveTab("materials")}
            id="nav-materials"
          >
            <span className="nav-icon">
              <IconMaterials />
            </span>
            <span className="nav-label">Course Materials</span>
          </button>

          <button
            className={`nav-item ${activeTab === "flashcards" ? "active" : ""}`}
            onClick={() => setActiveTab("flashcards")}
            id="nav-flashcards"
          >
            <span className="nav-icon">
              <IconFlashcards />
            </span>
            <span className="nav-label">Study Flashcards</span>
          </button>
        </nav>
      </aside>

      {/* Main Content Area */}
      <main className="app-main">
        {/* Top Header */}
        <header className="top-header">
          <div className="header-left">
            <h2 className="header-title">
              {activeTab === "chat" && "Academic Tutor"}
              {activeTab === "materials" && "Course Materials"}
              {activeTab === "flashcards" && "Study Flashcards"}
            </h2>
            {activeTab === "materials" && (
              <span className="header-subtitle">PDF, Markdown and Text Indexer</span>
            )}
            {activeTab === "flashcards" && (
              <span className="header-subtitle">Exam Preparation and Review Cards</span>
            )}
          </div>

          <div className="header-actions">
            {/* Dark / Light Mode Switcher */}
            <button
              className="theme-toggle-btn"
              onClick={toggleTheme}
              title={`Switch to ${theme === "dark" ? "Light" : "Dark"} mode`}
              id="theme-toggle"
            >
              {theme === "dark" ? <IconSun /> : <IconMoon />}
              <span>{theme === "dark" ? "Light Mode" : "Dark Mode"}</span>
            </button>

            <div className="pill-badge">
              <span>NVIDIA NIM / Nebius</span>
            </div>
          </div>
        </header>

        {/* --- VIEW 1: TUTOR CHAT --- */}
        {activeTab === "chat" && (
          <div className="view-container">
            <div className="chat-wrapper">
              <div className="chat-history">
                {messages.map((msg) => (
                  <div key={msg.id} className="message-card">
                    <div className={`avatar-icon ${msg.sender === "user" ? "user-avatar" : "tutor-avatar"}`}>
                      {msg.sender === "user" ? <IconUser /> : <IconTutor />}
                    </div>
                    <div className={`message-bubble ${msg.sender === "user" ? "user-bubble" : ""}`}>
                      <div className="message-meta">
                        <span className="author-name">{msg.sender === "user" ? "Student" : "Study Tutor"}</span>
                        <span>{msg.timestamp}</span>
                      </div>

                      <div className="message-content">
                        {msg.sender === "user" ? (
                          msg.text
                        ) : (
                          <FormattedMessage
                            text={msg.text}
                            chunks={msg.chunks}
                            onCitationClick={(src, chunk) => {
                              const chunkMatch =
                                chunk ||
                                msg.chunks?.find((c) => {
                                  const s = c.source || c.metadata?.source || "";
                                  return s && src.toLowerCase().includes(s.toLowerCase());
                                });
                              setSelectedCitation(
                                chunkMatch || { source: src, preview: "Cited in generated response." }
                              );
                            }}
                          />
                        )}
                      </div>

                      {/* Gemini-Style Sources Deck */}
                      {(() => {
                        if (msg.sender === "user") return null;

                        const citMatches = msg.text.match(/\[(?:Source:)[^\]]+\]|\(Source:[^)]+\)/gi) || [];
                        const seenKeys = new Set<string>();
                        const sourcesList: Array<{
                          index: number;
                          fileName: string;
                          page: string | null;
                          preview: string;
                          chunk?: any;
                          raw: string;
                        }> = [];

                        let idx = 1;
                        citMatches.forEach((raw) => {
                          const key = getCitationKey(raw);
                          if (!seenKeys.has(key)) {
                            seenKeys.add(key);
                            const m = raw.match(/Source:\s*([^,|\])]+)(?:[,|]\s*(?:Page:?|p\.?)\s*([^\])]+))?/i);
                            const file = m ? m[1].trim() : raw;
                            const page = m && m[2] ? m[2].trim() : null;

                            const matchedChunk =
                              msg.chunks?.find((c) => {
                                const src = (c.source || c.metadata?.source || "").toLowerCase();
                                const p = String(c.page || c.metadata?.page || "");
                                const fileMatches = file && src.includes(file.toLowerCase());
                                const pageMatches = !page || p === page;
                                return fileMatches && pageMatches;
                              }) ||
                              msg.chunks?.find((c) => {
                                const src = (c.source || c.metadata?.source || "").toLowerCase();
                                return file && src.includes(file.toLowerCase());
                              });

                            sourcesList.push({
                              index: idx++,
                              fileName: matchedChunk?.source || file,
                              page: matchedChunk?.page || page,
                              preview:
                                matchedChunk?.preview ||
                                matchedChunk?.text ||
                                "Verified academic source document excerpt.",
                              chunk: matchedChunk,
                              raw,
                            });
                          }
                        });

                        // Fallback: if no inline citations but msg.sources exists
                        if (sourcesList.length === 0 && msg.sources && msg.sources.length > 0) {
                          msg.sources.forEach((src, sIdx) => {
                            const chunkMatch = msg.chunks?.find((c) =>
                              src.includes(c.source || c.metadata?.source || "")
                            );
                            sourcesList.push({
                              index: sIdx + 1,
                              fileName: chunkMatch?.source || src,
                              page: chunkMatch?.page || null,
                              preview: chunkMatch?.preview || "Verified course material.",
                              chunk: chunkMatch,
                              raw: src,
                            });
                          });
                        }

                        if (sourcesList.length === 0) return null;

                        return (
                          <div className="gemini-sources-section">
                            <div className="gemini-sources-header">
                              <IconBook />
                              <span>Sources</span>
                              <span className="gemini-sources-count">{sourcesList.length}</span>
                            </div>
                            <div className="gemini-sources-grid">
                              {sourcesList.map((item) => (
                                <button
                                  key={item.index}
                                  type="button"
                                  className="gemini-source-card"
                                  onClick={() =>
                                    setSelectedCitation(
                                      item.chunk || { source: item.fileName, preview: item.preview }
                                    )
                                  }
                                  title={`Inspect source excerpt from ${item.fileName}`}
                                >
                                  <div className="gemini-card-top">
                                    <span className="gemini-card-num">{item.index}</span>
                                    <span className="gemini-card-title">{item.fileName}</span>
                                    {item.page && (
                                      <span className="gemini-card-page">p. {item.page}</span>
                                    )}
                                  </div>
                                  {item.preview && (
                                    <p className="gemini-card-snippet">{item.preview}</p>
                                  )}
                                </button>
                              ))}
                            </div>
                          </div>
                        );
                      })()}

                      {/* Latency & Model Footer */}
                      {msg.durationMs !== undefined && (
                        <div style={{ marginTop: "10px", fontSize: "11.5px", color: "var(--text-muted)" }}>
                          Generated in {msg.durationMs}ms via {msg.model || status.provider}
                        </div>
                      )}

                      {/* Obsidian Quick Actions */}
                      {msg.sender === "tutor" && msg.text && (
                        <div className="obsidian-action-row">
                          <button
                            type="button"
                            className={`obsidian-btn ${savedObsidianIds[msg.id] ? "saved" : ""}`}
                            onClick={() => handleSaveToObsidian(msg)}
                            title="Save note directly to local Obsidian Vault (Hamza)"
                          >
                            <span>
                              {savedObsidianIds[msg.id]
                                ? "✓ Saved to Obsidian (Hamza)"
                                : "📥 Save to Obsidian Vault"}
                            </span>
                          </button>
                          <button
                            type="button"
                            className="obsidian-btn"
                            onClick={() => {
                              navigator.clipboard.writeText(msg.text);
                              setCopiedNoteIds((prev) => ({ ...prev, [msg.id]: true }));
                              setTimeout(() => {
                                setCopiedNoteIds((prev) => ({ ...prev, [msg.id]: false }));
                              }, 2500);
                            }}
                            title="Copy full Obsidian Markdown to clipboard"
                          >
                            <span>
                              {copiedNoteIds[msg.id] ? "✓ Copied!" : "📋 Copy Note (.md)"}
                            </span>
                          </button>
                        </div>
                      )}
                    </div>
                  </div>
                ))}
                {isGenerating && (
                  <div className="message-card">
                    <div className="avatar-icon tutor-avatar">
                      <IconTutor />
                    </div>
                    <div className="message-bubble">
                      <div style={{ display: "flex", alignItems: "center", gap: "8px", color: "var(--text-secondary)" }}>
                        <span className="dot-pulse" />
                        <span>Searching materials and generating grounded answer...</span>
                      </div>
                    </div>
                  </div>
                )}
                <div ref={chatEndRef} />
              </div>

              {/* Chat Input Controls */}
              <div className="chat-controls-bar">
                <div className="input-options-row">
                  <div className="strategy-group">
                    <span style={{ fontSize: "12px", color: "var(--text-muted)" }}>Augmentation:</span>
                    <button
                      className={`strategy-btn ${augmentMode === "expand" ? "active" : ""}`}
                      onClick={() => setAugmentMode("expand")}
                      title="Multi-Query: Generates variations to capture diverse technical terminology"
                    >
                      Multi-Query
                    </button>
                    <button
                      className={`strategy-btn ${augmentMode === "rewrite" ? "active" : ""}`}
                      onClick={() => setAugmentMode("rewrite")}
                      title="Academic Rewrite: Converts informal queries into formal academic terminology"
                    >
                      Rewrite
                    </button>
                    <button
                      className={`strategy-btn ${augmentMode === "hyde" ? "active" : ""}`}
                      onClick={() => setAugmentMode("hyde")}
                      title="Deep Search: Passage-level semantic matching for complex questions"
                    >
                      Deep Search
                    </button>
                    <button
                      className={`strategy-btn ${useWeb ? "active" : ""}`}
                      onClick={() => setUseWeb(!useWeb)}
                      title="Web Search: Retrieve from Tavily web search in addition to course materials"
                    >
                      Web Search
                    </button>
                  </div>

                  <div style={{ fontSize: "11.5px", color: "var(--text-muted)" }}>
                    Press Enter to send
                  </div>
                </div>

                <div className="input-field-row">
                  <textarea
                    className="chat-textarea"
                    placeholder="Ask a question grounded in your course materials..."
                    value={inputQuery}
                    onChange={(e) => setInputQuery(e.target.value)}
                    onKeyDown={(e) => {
                      if (e.key === "Enter" && !e.shiftKey) {
                        e.preventDefault();
                        handleAsk();
                      }
                    }}
                    rows={2}
                    disabled={isGenerating}
                    id="chat-input"
                  />
                  <button
                    className="send-btn"
                    onClick={() => handleAsk()}
                    disabled={isGenerating || !inputQuery.trim()}
                    id="send-button"
                  >
                    <span>Ask</span>
                    <IconArrowRight />
                  </button>
                </div>

                {/* Prompt Starters */}
                <div className="prompt-starters">
                  <span style={{ fontSize: "11.5px", color: "var(--text-muted)", alignSelf: "center" }}>Quick Starters:</span>
                  <button
                    className="starter-pill"
                    onClick={() => handleAsk("What is Kepler's First Law and how does eccentricity affect it?")}
                  >
                    Kepler's First Law
                  </button>
                  <button
                    className="starter-pill"
                    onClick={() => handleAsk("Explain the relationship between orbital period and semi-major axis in Kepler's Third Law")}
                  >
                    Third Law Formula
                  </button>
                  <button
                    className="starter-pill"
                    onClick={() => handleAsk("What is the difference between perihelion and aphelion speed?")}
                  >
                    Perihelion vs Aphelion
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* --- VIEW 2: COURSE MATERIALS --- */}
        {activeTab === "materials" && (
          <div className="view-container">
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "18px" }}>
              <div>
                <h3 style={{ fontSize: "18px", fontWeight: "700" }}>Course Materials</h3>
                <p style={{ color: "var(--text-muted)", fontSize: "13.5px" }}>
                  Upload academic documents (.pdf, .txt, .md). Every chunk is indexed in ChromaDB with page tracking.
                </p>
              </div>
              <div style={{ display: "flex", gap: "8px" }}>
                <button className="btn-secondary" onClick={handleIngestSample} disabled={isUploading} id="load-sample-btn">
                  Load Kepler Sample
                </button>
                <button className="btn-danger" onClick={handleClearDatabase}>
                  <IconTrash />
                  <span>Clear Index</span>
                </button>
              </div>
            </div>

            {uploadMessage && (
              <div
                style={{
                  padding: "12px 16px",
                  borderRadius: "8px",
                  marginBottom: "16px",
                  background:
                    uploadMessage.type === "success"
                      ? "var(--accent-success-subtle)"
                      : uploadMessage.type === "info"
                      ? "rgba(99, 102, 241, 0.12)"
                      : "var(--accent-danger-subtle)",
                  border: `1px solid ${
                    uploadMessage.type === "success"
                      ? "var(--accent-success-border)"
                      : uploadMessage.type === "info"
                      ? "rgba(99, 102, 241, 0.35)"
                      : "var(--accent-danger-border)"
                  }`,
                  color:
                    uploadMessage.type === "success"
                      ? "var(--accent-success)"
                      : uploadMessage.type === "info"
                      ? "#6366f1"
                      : "var(--accent-danger)",
                  fontSize: "13px",
                  fontWeight: 500,
                  display: "flex",
                  alignItems: "center",
                  gap: "10px",
                }}
              >
                {uploadMessage.type === "info" && (
                  <span
                    style={{
                      width: "8px",
                      height: "8px",
                      borderRadius: "50%",
                      backgroundColor: "#6366f1",
                      display: "inline-block",
                      animation: "pulse 1.5s infinite",
                    }}
                  />
                )}
                <span>{uploadMessage.text}</span>
              </div>
            )}

            {/* Drag & Drop Upload Zone */}
            <div
              className="dropzone-card"
              onClick={() => fileInputRef.current?.click()}
            >
              <input
                ref={fileInputRef}
                type="file"
                accept=".pdf,.txt,.md,.png,.jpg,.jpeg"
                style={{ display: "none" }}
                onChange={handleFileUpload}
              />
              <div className="dropzone-icon">
                <IconUpload />
              </div>
              <div className="dropzone-title">
                {isUploading ? "Processing document chunks..." : "Click or drag course files here"}
              </div>
              <div className="dropzone-sub">
                Supported: PDF lecture slides, diagrams (.png, .jpg), TXT transcripts, and Markdown notes
              </div>
            </div>

            {/* Document Table */}
            <div className="doc-table-card">
              <div className="table-header">
                <span className="table-title">Indexed Documents ({documents.length})</span>
                <span style={{ fontSize: "12px", color: "var(--text-muted)" }}>
                  Verified course library
                </span>
              </div>

              {documents.length === 0 ? (
                <div style={{ padding: "36px", textAlign: "center", color: "var(--text-muted)", fontSize: "13.5px" }}>
                  No materials indexed yet. Upload a file or click &quot;Load Kepler Sample&quot; to begin.
                </div>
              ) : (
                documents.map((doc, idx) => (
                  <div key={idx} className="doc-item">
                    <div className="doc-name">
                      <IconFile />
                      <div>
                        <div>{doc.source}</div>
                        <div style={{ fontSize: "11.5px", color: "var(--text-muted)" }}>{doc.file_path}</div>
                      </div>
                    </div>
                    <div style={{ display: "flex", gap: "8px", alignItems: "center" }}>
                      {doc.has_visuals && (
                        <span
                          style={{
                            fontSize: "11px",
                            fontWeight: 600,
                            padding: "2px 8px",
                            borderRadius: "4px",
                            background: "rgba(99, 102, 241, 0.15)",
                            color: "#6366f1",
                            border: "1px solid rgba(99, 102, 241, 0.3)",
                          }}
                        >
                          {doc.visual_count ? `${doc.visual_count} Diagram(s)` : "Diagrams"}
                        </span>
                      )}
                      <span className="doc-badge-pill">{doc.page_count} page(s)</span>
                      <span className="doc-badge-pill" style={{ color: "var(--accent-success)" }}>
                        Indexed
                      </span>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        )}

        {/* --- VIEW 3: STUDY FLASHCARDS --- */}
        {activeTab === "flashcards" && (
          <div className="view-container">
            <div className="flashcards-header">
              <div>
                <h3 style={{ fontSize: "18px", fontWeight: "700" }}>Study Flashcards</h3>
                <p style={{ color: "var(--text-muted)", fontSize: "13.5px" }}>
                  Generate exam-ready question-and-answer study cards derived strictly from course materials.
                </p>
              </div>

              <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
                <input
                  type="text"
                  placeholder="Focus topic (optional)..."
                  value={flashcardTopic}
                  onChange={(e) => setFlashcardTopic(e.target.value)}
                  style={{
                    background: "var(--bg-input)",
                    border: "1px solid var(--border-subtle)",
                    borderRadius: "6px",
                    padding: "7px 10px",
                    color: "var(--text-primary)",
                    fontSize: "12.5px",
                    width: "200px",
                  }}
                />
                <button
                  className="btn-primary"
                  onClick={handleGenerateFlashcards}
                  disabled={isGeneratingCards}
                  id="generate-cards-btn"
                >
                  {isGeneratingCards ? "Generating..." : "Generate Cards"}
                </button>
              </div>
            </div>

            {flashcards.length === 0 ? (
              <div
                style={{
                  marginTop: "32px",
                  padding: "48px 20px",
                  textAlign: "center",
                  background: "var(--bg-card)",
                  borderRadius: "12px",
                  border: "1px solid var(--border-subtle)",
                }}
              >
                <div style={{ marginBottom: "12px", display: "flex", justifyContent: "center", color: "var(--text-muted)" }}>
                  <IconFlashcards />
                </div>
                <h4 style={{ fontSize: "16px", fontWeight: "600", marginBottom: "6px" }}>No Flashcards Generated Yet</h4>
                <p style={{ color: "var(--text-muted)", fontSize: "13px", maxWidth: "420px", margin: "0 auto" }}>
                  Click &quot;Generate Cards&quot; to transform your indexed study materials into study review cards.
                </p>
              </div>
            ) : (
              <div className="cards-grid" style={{ marginTop: "20px" }}>
                {flashcards.map((card) => {
                  const isFlipped = flippedCards[card.id];
                  const score = cardScores[card.id];
                  return (
                    <div
                      key={card.id}
                      className={`flip-card ${isFlipped ? "flipped" : ""}`}
                      onClick={() => toggleCardFlip(card.id)}
                    >
                      <div className="flip-card-inner">
                        {/* Front: Question */}
                        <div className="flip-card-front">
                          <div className="card-top">
                            <span className="card-category">{card.category}</span>
                            <span className="card-difficulty">{card.difficulty}</span>
                          </div>
                          <div className="card-question">{card.question}</div>
                          <div className="card-hint">
                            <IconFlip />
                            <span>Click card to reveal answer</span>
                            {score && (
                              <span style={{ marginLeft: "auto", fontWeight: "600", color: score === "mastered" ? "var(--accent-success)" : "var(--accent-warning)" }}>
                                {score === "mastered" ? "Mastered" : "Needs Review"}
                              </span>
                            )}
                          </div>
                        </div>

                        {/* Back: Grounded Answer */}
                        <div className="flip-card-back">
                          <div className="card-top">
                            <span className="card-category" style={{ color: "var(--accent-primary)" }}>
                              Explanation & Formula
                            </span>
                            <span style={{ fontSize: "11px", color: "var(--text-muted)" }}>{card.source}</span>
                          </div>
                          <div className="card-answer">{card.answer}</div>
                          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                            <button
                              className="btn-secondary"
                              style={{ padding: "4px 8px", fontSize: "11px" }}
                              onClick={(e) => scoreCard(card.id, "review", e)}
                            >
                              Needs Review
                            </button>
                            <button
                              className="btn-primary"
                              style={{ padding: "4px 10px", fontSize: "11px" }}
                              onClick={(e) => scoreCard(card.id, "mastered", e)}
                            >
                              Mastered
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        )}
      </main>

      {/* Citation Inspector Modal */}
      {selectedCitation && (
        <div className="modal-overlay" onClick={() => setSelectedCitation(null)}>
          <div className="modal-card" onClick={(e) => e.stopPropagation()}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "14px" }}>
              <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
                <IconBook />
                <h4 style={{ fontSize: "15px", fontWeight: "700" }}>
                  Verified Source Excerpt
                </h4>
              </div>
              <button
                style={{ background: "none", border: "none", color: "var(--text-muted)", fontSize: "16px", cursor: "pointer" }}
                onClick={() => setSelectedCitation(null)}
              >
                ✕
              </button>
            </div>

            <div style={{ marginBottom: "12px", fontSize: "12.5px", color: "var(--accent-primary)", display: "flex", justifyContent: "space-between", alignItems: "center", flexWrap: "wrap", gap: "6px" }}>
              <span>
                Document: {selectedCitation.source || selectedCitation.metadata?.source || "Unknown"}
                {selectedCitation.page && ` · Page ${selectedCitation.page}`}
              </span>
              {(selectedCitation.metadata?.has_visuals || (selectedCitation.preview || selectedCitation.text || "").includes("[Visual Diagram") || (selectedCitation.preview || selectedCitation.text || "").includes("[Lecture Slide")) && (
                <span
                  style={{
                    fontSize: "11px",
                    fontWeight: 600,
                    padding: "2px 8px",
                    borderRadius: "4px",
                    background: "rgba(99, 102, 241, 0.15)",
                    color: "#6366f1",
                    border: "1px solid rgba(99, 102, 241, 0.3)",
                  }}
                >
                  Diagram / Visual Context
                </span>
              )}
            </div>

            <div
              style={{
                background: "var(--bg-primary)",
                padding: "14px",
                borderRadius: "8px",
                border: "1px solid var(--border-subtle)",
                fontSize: "13px",
                lineHeight: "1.6",
                color: "var(--text-secondary)",
                maxHeight: "240px",
                overflowY: "auto",
              }}
            >
              {selectedCitation.preview || selectedCitation.text || "No preview excerpt available."}
            </div>

            {selectedCitation.score !== undefined && (
              <div style={{ marginTop: "10px", fontSize: "11.5px", color: "var(--text-muted)" }}>
                Relevance Confidence: {(selectedCitation.score * 100).toFixed(1)}%
              </div>
            )}

            <div style={{ marginTop: "18px", display: "flex", justifyContent: "flex-end" }}>
              <button className="btn-secondary" onClick={() => setSelectedCitation(null)}>
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
