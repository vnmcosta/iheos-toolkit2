package gov.nist.toolkit.xdstools2.client.inspector.mvp;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import gov.nist.toolkit.registrymetadata.client.DocumentEntry;
import gov.nist.toolkit.xdstools2.client.util.AnnotatedItem;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

abstract class DocEntryDataTable extends DataTable<DocumentEntry> implements IsWidget {

    private FlowPanel widgetPanel = new FlowPanel();
    private static String STATUS_COLUMN_NAME = "Status";
    private static String ID_COLUMN_NAME = "Id";
    private static String LID_COLUMN_NAME = "Lid";
    private static String UNIQUE_ID_COLUMN_NAME = "UniqueId";
    private static String VERSION_COLUMN_NAME = "Version";
    private static String TITLE_COLUMN_NAME = "Title";
    private static String HOME_ID_COLUMN_NAME = "HomeId";
    private static String REPOSITORY_UNIQUE_ID = "ReposId";
    private static String MIME_TYPE_COLUMN_NAME = "MimeType";
    private static String HASH_COLUMN_NAME = "Hash";
    private static String SIZE_COLUMN_NAME = "Size";
    private FlowPanel columnSelectionPanel = new FlowPanel();

    private static List<AnnotatedItem> columnList = Arrays.asList(
            new AnnotatedItem(true, STATUS_COLUMN_NAME),
            new AnnotatedItem(false, ID_COLUMN_NAME),
            new AnnotatedItem(true, LID_COLUMN_NAME),
            new AnnotatedItem(true, UNIQUE_ID_COLUMN_NAME),
            new AnnotatedItem(true, VERSION_COLUMN_NAME),
            new AnnotatedItem(false, TITLE_COLUMN_NAME),
            new AnnotatedItem(false, HOME_ID_COLUMN_NAME),
            new AnnotatedItem(false, REPOSITORY_UNIQUE_ID),
            new AnnotatedItem(true, MIME_TYPE_COLUMN_NAME),
            new AnnotatedItem(false, HASH_COLUMN_NAME),
            new AnnotatedItem(false, SIZE_COLUMN_NAME)
    );

    private static final ProvidesKey<DocumentEntry> KEY_PROVIDER = new ProvidesKey<DocumentEntry>() {
        @Override
        public Object getKey(DocumentEntry item) {
            return item == null ? null : item.id;
        }
    };

    public DocEntryDataTable(int pageSize) {
        super(columnList, pageSize, new DocumentEntry(), true, false);
        widgetPanel.add(columnSelectionPanel);
        widgetPanel.add(super.asWidget());
    }


    @Override
    void addTableColumns() {

        if (compareSelect.getValue()) {
            dataTable.addColumn(new Column<DocumentEntry, Boolean>(new CheckboxCell(true, false)) {
                @Override
                public Boolean getValue(DocumentEntry object) {
                        return dataTable.getSelectionModel().isSelected(object);
                }

                @Override
                public void render(Cell.Context context, DocumentEntry object, SafeHtmlBuilder sb) {
                    super.render(context, object, sb);

                }
            }, "Select");
        }

        if (columnToBeDisplayedIsChecked(STATUS_COLUMN_NAME)) {
            TextColumn<DocumentEntry> statusColumn = new TextColumn<DocumentEntry>() {
                @Override
                public String getValue(DocumentEntry de) {
                    return de.status.toString();
                }

            };
            statusColumn.setSortable(true);
            columnSortHandler.setComparator(statusColumn,
                    new Comparator<DocumentEntry>() {
                        public int compare(DocumentEntry o1, DocumentEntry o2) {
                            if (o1 == o2) {
                                return 0;
                            }

                            if (o1 != null && o1.status != null) {
                                return (o2 != null && o2.status != null) ? o1.status.toString().compareTo(o2.status.toString()) : 1;
                            }
                            return -1;
                        }
                    });
            dataTable.addColumn(statusColumn, STATUS_COLUMN_NAME);
        }
        if (columnToBeDisplayedIsChecked(ID_COLUMN_NAME)) {
            TextColumn<DocumentEntry> idColumn = new TextColumn<DocumentEntry>() {
                @Override
                public String getValue(DocumentEntry de) {
                    return de.id.toString();
                }

            };
            idColumn.setSortable(true);
            columnSortHandler.setComparator(idColumn,
                    new Comparator<DocumentEntry>() {
                        public int compare(DocumentEntry o1, DocumentEntry o2) {
                            if (o1 == o2) {
                                return 0;
                            }

                            if (o1 != null && o1.id != null) {
                                return (o2 != null && o2.id != null) ? o1.id.toString().compareTo(o2.id.toString()) : 1;
                            }
                            return -1;
                        }
                    });
            dataTable.addColumn(idColumn, ID_COLUMN_NAME);
        }
        if (columnToBeDisplayedIsChecked(LID_COLUMN_NAME)) {
            TextColumn<DocumentEntry> lidColumn = new TextColumn<DocumentEntry>() {
                @Override
                public String getValue(DocumentEntry de) {
                    return de.lid.toString();
                }

            };
            lidColumn.setSortable(true);
            columnSortHandler.setComparator(lidColumn,
                    new Comparator<DocumentEntry>() {
                        public int compare(DocumentEntry o1, DocumentEntry o2) {
                            if (o1 == o2) {
                                return 0;
                            }

                            if (o1 != null && o1.lid != null) {
                                return (o2 != null && o2.lid != null) ? o1.lid.toString().compareTo(o2.lid.toString()) : 1;
                            }
                            return -1;
                        }
                    });
            dataTable.addColumn(lidColumn, LID_COLUMN_NAME);
        }
        if (columnToBeDisplayedIsChecked(UNIQUE_ID_COLUMN_NAME)) {
            TextColumn<DocumentEntry> uniqueIdColumn = new TextColumn<DocumentEntry>() {
                @Override
                public String getValue(DocumentEntry de) {
                    return de.uniqueId;
                }

            };
            uniqueIdColumn.setSortable(true);
            columnSortHandler.setComparator(uniqueIdColumn,
                    new Comparator<DocumentEntry>() {
                        public int compare(DocumentEntry o1, DocumentEntry o2) {
                            if (o1 == o2) {
                                return 0;
                            }

                            if (o1 != null && o1.uniqueId!= null) {
                                return (o2 != null && o2.uniqueId!= null) ? o1.uniqueId.compareTo(o2.uniqueId) : 1;
                            }
                            return -1;
                        }
                    });
            dataTable.addColumn(uniqueIdColumn, UNIQUE_ID_COLUMN_NAME);
        }
        if (columnToBeDisplayedIsChecked(VERSION_COLUMN_NAME)) {
            TextColumn<DocumentEntry> versionColumn = new TextColumn<DocumentEntry>() {
                @Override
                public String getValue(DocumentEntry de) {
                    return de.version.toString();
                }

            };
            versionColumn.setSortable(true);
            columnSortHandler.setComparator(versionColumn,
                    new Comparator<DocumentEntry>() {
                        public int compare(DocumentEntry o1, DocumentEntry o2) {
                            if (o1 == o2) {
                                return 0;
                            }

                            if (o1 != null && o1.version!= null) {
                                return (o2 != null && o2.version != null) ? o1.version.toString().compareTo(o2.version.toString()) : 1;
                            }
                            return -1;
                        }
                    });
            dataTable.addColumn(versionColumn, VERSION_COLUMN_NAME);
        }
        if (columnToBeDisplayedIsChecked(TITLE_COLUMN_NAME)) {
            TextColumn<DocumentEntry> titleColumn = new TextColumn<DocumentEntry>() {
                @Override
                public String getValue(DocumentEntry de) {
                    return de.title;
                }
            };
            titleColumn.setSortable(true);
            columnSortHandler.setComparator(titleColumn,
                    new Comparator<DocumentEntry>() {
                        public int compare(DocumentEntry o1, DocumentEntry o2) {
                            if (o1 == o2) {
                                return 0;
                            }

                            if (o1 != null && o1.title!=null) {
                                return (o2 != null && o2.title!=null) ? o1.title.toString().compareTo(o2.title.toString()) : 1;
                            }
                            return -1;
                        }
                    });

            dataTable.addColumn(titleColumn,TITLE_COLUMN_NAME);
        }
        if (columnToBeDisplayedIsChecked(HOME_ID_COLUMN_NAME)) {
            TextColumn<DocumentEntry> homeColumn = new TextColumn<DocumentEntry>() {
                @Override
                public String getValue(DocumentEntry de) {
                    return de.home;
                }
            };
            homeColumn.setSortable(true);
            columnSortHandler.setComparator(homeColumn,
                    new Comparator<DocumentEntry>() {
                        public int compare(DocumentEntry o1, DocumentEntry o2) {
                            if (o1 == o2) {
                                return 0;
                            }

                            if (o1 != null && o1.home!=null) {
                                return (o2 != null && o2.home!=null) ? o1.home.toString().compareTo(o2.home.toString()) : 1;
                            }
                            return -1;
                        }
                    });

            dataTable.addColumn(homeColumn,HOME_ID_COLUMN_NAME);
        }

        if (columnToBeDisplayedIsChecked(REPOSITORY_UNIQUE_ID)) {
            TextColumn<DocumentEntry> reposColumn = new TextColumn<DocumentEntry>() {
                @Override
                public String getValue(DocumentEntry de) {
                    return de.repositoryUniqueId;
                }
            };
            reposColumn.setSortable(true);
            columnSortHandler.setComparator(reposColumn,
                    new Comparator<DocumentEntry>() {
                        public int compare(DocumentEntry o1, DocumentEntry o2) {
                            if (o1 == o2) {
                                return 0;
                            }

                            if (o1 != null && o1.repositoryUniqueId!=null) {
                                return (o2 != null && o2.repositoryUniqueId!=null) ? o1.repositoryUniqueId.toString().compareTo(o2.repositoryUniqueId.toString()) : 1;
                            }
                            return -1;
                        }
                    });

            dataTable.addColumn(reposColumn,REPOSITORY_UNIQUE_ID);
        }

        if (columnToBeDisplayedIsChecked(MIME_TYPE_COLUMN_NAME)) {
            TextColumn<DocumentEntry> mimeTypeColumn = new TextColumn<DocumentEntry>() {
                @Override
                public String getValue(DocumentEntry de) {
                    return de.mimeType;
                }
            };
            mimeTypeColumn.setSortable(true);
            columnSortHandler.setComparator(mimeTypeColumn,
                    new Comparator<DocumentEntry>() {
                        public int compare(DocumentEntry o1, DocumentEntry o2) {
                            if (o1 == o2) {
                                return 0;
                            }

                            if (o1 != null && o1.mimeType!=null) {
                                return (o2 != null && o2.mimeType!=null) ? o1.mimeType.toString().compareTo(o2.mimeType.toString()) : 1;
                            }
                            return -1;
                        }
                    });

            dataTable.addColumn(mimeTypeColumn,MIME_TYPE_COLUMN_NAME);
        }



        // hash
        if (columnToBeDisplayedIsChecked(HASH_COLUMN_NAME)) {
            TextColumn<DocumentEntry> hashColumn = new TextColumn<DocumentEntry>() {
                @Override
                public String getValue(DocumentEntry de) {
                    return de.hash;
                }
            };
            hashColumn.setSortable(true);
            columnSortHandler.setComparator(hashColumn,
                    new Comparator<DocumentEntry>() {
                        public int compare(DocumentEntry o1, DocumentEntry o2) {
                            if (o1 == o2) {
                                return 0;
                            }

                            if (o1 != null && o1.hash!=null) {
                                return (o2 != null && o2.hash!=null) ? o1.hash.toString().compareTo(o2.hash.toString()) : 1;
                            }
                            return -1;
                        }
                    });

            dataTable.addColumn(hashColumn,HASH_COLUMN_NAME);
        }


        // size
        if (columnToBeDisplayedIsChecked(SIZE_COLUMN_NAME)) {
            TextColumn<DocumentEntry> sizeColumn = new TextColumn<DocumentEntry>() {
                @Override
                public String getValue(DocumentEntry de) {
                    return de.size;
                }
            };
            sizeColumn.setSortable(true);
            columnSortHandler.setComparator(sizeColumn,
                    new Comparator<DocumentEntry>() {
                        public int compare(DocumentEntry o1, DocumentEntry o2) {
                            if (o1 == o2) {
                                return 0;
                            }

                            if (o1 != null && o1.size!=null) {
                                try {
                                    if (o2!=null && o2.size!=null) {
                                       return new Integer(o1.size).compareTo(new Integer(o2.size));
                                    }
                                    return 1;
                                } catch (Exception ex) {
                                    GWT.log(ex.toString());
                                }
                            } else if (o2!=null && o2.size!=null) {
                                return 1;
                            }
                            return -1;
                        }
                    });

            dataTable.addColumn(sizeColumn,SIZE_COLUMN_NAME);
        }




    }



    @Override
    void addActionBtnTableColumns() {
        // Only the first 10 can be queried due to the max params limit in stored query
    }

    @Override
    ProvidesKey<DocumentEntry> getKeyProvider() {
        return KEY_PROVIDER;
    }


    void setData(List<DocumentEntry> deList) {
        dataProvider.getList().clear();
        if (deList!=null) {
            compareSelect.setEnabled(deList.size()>1);
            dataProvider.getList().addAll(deList);
        }
    }


    @Override
    public Widget asWidget() {
        return widgetPanel;
    }
}
