# Visual Studio Code

## Instalar Prettier

[Prettier](https://prettier.io/)

### Passos

- Vá na aba de extensões
- Pesquise por "Prettier - Code formatter"
- Clique em "Instalar"
- Pronto com a extensão instalada o VS Code vai usar as configurações do arquivo ".prettierrc"

## Instalar EditorConfig

[EditorConfig](https://editorconfig.org/)

## Passos

- Vá na aba de extensões
- Pesquise por "EditorConfig for VS Code"
- Clique em "Instalar"
- Pronto com a extensão instalada o VS Code vai usar as configurações do arquivo ".editorconfig"

## Instalar ESLint

[ESLint](https://eslint.org/)

## Passos

- Vá na aba de extensões
- Pesquise por "ESLint"
- Clique em "Instalar"
- Habilite o ESLint no VS Code
  - Ctrl + Shift + P ou CMD + Shift + P
  - Pesquise por 'ESLint Enable'
  - Clique para habilitar
- Pronto com a extensão instalada o VS Code vai usar as configurações do arquivo ".eslintrc.yml"

## Instalar psioniq File Header

[Instalar psioniq File Header](https://marketplace.visualstudio.com/items?itemName=psioniq.psi-header)

## Passos

- Vá na aba de extensões
- Pesquise por "psioniq File Header"
- Clique em "Instalar"
- Habilite a formatação padrão

  - Ctrl + Shift + P ou CMD + Shift + P
  - Pesquise por "Open Settings (JSON)"
  - A partir da última configuração adicione o seguinte código:

  ```
  "psi-header.config": {
    "forceToTop": true
  },
  "psi-header.changes-tracking": {
    "autoHeader": "autoSave",
    "include": ["typescript"],
    "enforceHeader": true,
    "excludeGlob": ["jsonc", "json"]
  },
  "psi-header.variables": [["company", "Repositorio Virtual"]],
  "psi-header.templates": [
    {
      "language": "*",
      "template": [
        "Description: FIXME: Document this type",
        "Project: <<projectname>>",
        "",
        "author: <<author>>",
        "date: <<date>> <<time>>",
        "version $"
      ]
    }
  ]
  ```

  - Pronto agora ao criar um novo arquivo com do TypeScript o comentário padrão será adicionado

## Outras configurações

- Ctrl + Shift + P ou CMD + Shift + P
- Pesquisar por 'Open User Settings'
- Habilite a formatação ao salvar
  - Na aba 'User'
  - Vá em 'Text Editor' > Formatting
  - Marque a opção 'Format On Save'
